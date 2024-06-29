package com.example.reciipiie

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reciipiie.adapters.SearchItemAdapter
import com.example.reciipiie.api.RandomRecipeApi
import com.example.reciipiie.api.RetrofitHelper
import com.example.reciipiie.models.Item
import com.example.reciipiie.models.SearchModel
import com.example.reciipiie.repository.RecipeRepository
import com.example.reciipiie.viewmodels.MainViewModel
import com.example.reciipiie.viewmodels.MainViewModelFactory
import java.util.Locale


class SearchActivity : AppCompatActivity() {
    private lateinit var adapter: SearchItemAdapter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var recyclerView: RecyclerView
    private var filteredList: MutableList<SearchModel> = mutableListOf()

    private lateinit var mainViewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchView = findViewById(R.id.recipe_search_view)
        adapter = SearchItemAdapter(filteredList)
        recyclerView = findViewById(R.id.search_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Initialize ViewModel
        val searchRecipeService = RetrofitHelper.getInstance().create(RandomRecipeApi::class.java)
        val repository = RecipeRepository(searchRecipeService)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))
            .get(MainViewModel::class.java)


        // Observe recipe details from ViewModel
        mainViewModel.searchRecipe.observe(this, Observer { recipe ->

              // Update UI with recipe details
            filteredList.clear()
            filteredList.addAll(recipe.results)
            if (filteredList.isEmpty()) {
                Toast.makeText(this, " NO Data Found", Toast.LENGTH_SHORT).show()
                recyclerView.visibility = RecyclerView.GONE
            } else {

                recyclerView.visibility = RecyclerView.VISIBLE
                adapter.notifyDataSetChanged()
            }
        })

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@SearchActivity, "I'm in search activity", Toast.LENGTH_SHORT).show()
                loadSearchList(newText)
                return true
            }
        })
    }

    private fun loadSearchList(newText: String?) {
        if (newText.isNullOrEmpty()) {

            recyclerView.visibility = RecyclerView.GONE
        } else {
            mainViewModel.fetchSearchRecipeDetails(newText,10)
        }
    }
}