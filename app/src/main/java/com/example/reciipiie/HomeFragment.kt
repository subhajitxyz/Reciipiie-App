package com.example.reciipiie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciipiie.adapters.RandomRecipeAdapter
import com.example.reciipiie.adapters.VerticleRecipeAdapter
import com.example.reciipiie.api.RandomRecipeApi
import com.example.reciipiie.api.RetrofitHelper
import com.example.reciipiie.databinding.FragmentHomeBinding
import com.example.reciipiie.models.RandomRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchModel
import com.example.reciipiie.repository.RecipeRepository
import com.example.reciipiie.viewmodels.MainViewModel
import com.example.reciipiie.viewmodels.MainViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var popularRecyclerView: RecyclerView
    private var popularRecipeModelList = ArrayList<RandomRecipeModel>()
    private var popularRecipeList= ArrayList<Recipe>()

    private lateinit var mainViewModel2: MainViewModel
    private lateinit var allRecipeRecyclerView: RecyclerView
    private var allRecipeModelList: MutableList<SearchModel> = mutableListOf()
    private var allRecipeList= ArrayList<Recipe>()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


        auth = FirebaseAuth.getInstance()
        // Get the currently signed-in user
        val currentUser: FirebaseUser? = auth.currentUser
        // Display the user's name
        val userName = currentUser?.displayName
        binding.usernameText.text = "Hey "+userName

        popularRecyclerView = binding.popularRecipeRecView
        popularRecyclerView.setHasFixedSize(true)
        popularRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        val adapter = RandomRecipeAdapter(requireContext(),popularRecipeList)
        popularRecyclerView.adapter = adapter

        // ViewModel setup
        val recipeService = RetrofitHelper.getInstance().create(RandomRecipeApi::class.java)
        val repository = RecipeRepository(recipeService)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))
            .get(MainViewModel::class.java)

        // Observing ViewModel data
        mainViewModel.products.observe(viewLifecycleOwner, Observer {
            popularRecipeModelList.clear()
            popularRecipeModelList.add(it)
            for(list in popularRecipeModelList){
                for( j in list.recipes){
                    popularRecipeList.add(j)
                }
            }
            adapter.notifyDataSetChanged()
        })




        allRecipeRecyclerView = binding.allRecipeRecView
        allRecipeRecyclerView.setHasFixedSize(true)
        allRecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter2 = VerticleRecipeAdapter(requireContext(),allRecipeModelList)
        allRecipeRecyclerView.adapter = adapter2

        val recipeService2 = RetrofitHelper.getInstance().create(RandomRecipeApi::class.java)
        val repository2 = RecipeRepository(recipeService2)
        mainViewModel2 = ViewModelProvider(this, MainViewModelFactory(repository2))
            .get(MainViewModel::class.java)

        mainViewModel2.fetchSearchRecipeDetails("Indian",40)
        // Observing ViewModel data
        mainViewModel2.searchRecipe.observe(viewLifecycleOwner, Observer {
            allRecipeModelList.clear()
            allRecipeModelList.addAll(it.results)

            adapter2.notifyDataSetChanged()
        })




        // Handle edge-to-edge display if needed
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.recipeSearchView.setOnClickListener {
            startActivity(Intent(requireContext(),SearchActivity::class.java))
        }

        return view
    }


}