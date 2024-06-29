package com.example.reciipiie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciipiie.adapters.VerticleRecipeAdapter
import com.example.reciipiie.dao.FavoriteRecipeDatabase
import com.example.reciipiie.databinding.FragmentFavouriteBinding
import com.example.reciipiie.databinding.FragmentHomeBinding
import com.example.reciipiie.models.FavoriteRecipeModel
import com.example.reciipiie.models.SearchModel
import com.google.android.material.bottomsheet.BottomSheetDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavouriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FavouriteFragment : Fragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteRecipeDatabase: FavoriteRecipeDatabase
    private var favoriteRecipeList: MutableList<SearchModel> = mutableListOf()
    private lateinit var favoriteRecipeRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding.root

        favoriteRecipeRecyclerView = binding.favoriteRecView
        favoriteRecipeRecyclerView.setHasFixedSize(true)
        favoriteRecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = VerticleRecipeAdapter(requireContext(), favoriteRecipeList)
        favoriteRecipeRecyclerView.adapter = adapter

        favoriteRecipeDatabase = FavoriteRecipeDatabase.getDatabase(requireContext())
        favoriteRecipeDatabase.favoriteRecipeDao().getAllRecipeDetails().observe(viewLifecycleOwner,
            Observer { recipes ->
                favoriteRecipeList.clear()
                recipes.forEach { recipe ->
                    favoriteRecipeList.add(SearchModel(recipe.recipeId, recipe.recipeImage, "", recipe.recipeTitle))
                }
                adapter.notifyDataSetChanged()
            })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}