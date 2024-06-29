package com.example.reciipiie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.reciipiie.adapters.IngredientAdapter
import com.example.reciipiie.api.RandomRecipeApi
import com.example.reciipiie.api.RetrofitHelper
import com.example.reciipiie.dao.FavoriteRecipeDatabase
import com.example.reciipiie.databinding.ActivityMainBinding
import com.example.reciipiie.databinding.ActivityRecipeDetailsBinding
import com.example.reciipiie.models.FavoriteRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchModel
import com.example.reciipiie.repository.RecipeRepository
import com.example.reciipiie.viewmodels.MainViewModel
import com.example.reciipiie.viewmodels.MainViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeDetailsActivity : AppCompatActivity() {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeInformation: Recipe
    private lateinit var binding :ActivityRecipeDetailsBinding
    private var real_Id: Int=0

    private lateinit var favoriteRecipeDatabase: FavoriteRecipeDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val intent = intent
        if (intent != null && intent.hasExtra("recipe_id")) {
            val recipeId = intent.getIntExtra("recipe_id", 1)
            // Use recipeId to fetch recipe details or perform any action


            val recipeDetailsService = RetrofitHelper.getInstance().create(RandomRecipeApi::class.java)
            val repository = RecipeRepository(recipeDetailsService)
            mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))
                .get(MainViewModel::class.java)

            // Observe recipe details from ViewModel

            mainViewModel.fetchRecipeDetails(recipeId)
            mainViewModel.recipe.observe(this, Observer { recipe ->
                // Update UI with recipe details
                recipeInformation = recipe
                real_Id=recipeInformation.id

                loadEverything(recipeInformation)

            })

            // Fetch recipe details based on recipeId

        }




    }
    fun loadEverything(recipeInformation: Recipe) {
        var current_Id = recipeInformation.id
        favoriteRecipeDatabase = FavoriteRecipeDatabase.getDatabase(this)

        favoriteRecipeDatabase.favoriteRecipeDao().getAllRecipeDetails().observe(this, Observer { recipes ->
            val isPresent = recipes.any { it.recipeId == current_Id }
            binding.addToWishlistBtn.isChecked = isPresent
        })

        

        // Existing UI update code
        Glide.with(this@RecipeDetailsActivity)
            .load(recipeInformation.image)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(50)))
            .placeholder(R.drawable.placeholder_icon)
            .into(binding.recipeItemImageView)

        binding.recipeItemName.text = recipeInformation.title
        binding.recipeTime.text = "${recipeInformation.readyInMinutes} min"
        binding.servingNo.text = recipeInformation.servings.toString()
        binding.price.text = recipeInformation.pricePerServing.toString()

        val ingredientList = recipeInformation.extendedIngredients
        binding.ingridientsRecView.layoutManager = LinearLayoutManager(this@RecipeDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
        val adapter = IngredientAdapter(this, ingredientList)
        binding.ingridientsRecView.adapter = adapter

        binding.instructionsDetails.text = recipeInformation.instructions
        binding.recipeQuickSummary.text = recipeInformation.summary
        binding.Nutrition.text = "got Nothing"

        if (recipeInformation.veryHealthy) {
            binding.badForHealthTextView.text = "Very Healthy Recipe"
        } else {
            binding.badForHealthTextView.text = "Not very healthy"
        }

        binding.goodForHealthTextView.text = "Health Score of this dish is ${recipeInformation.healthScore}"
    }

    override fun onBackPressed() {
        super.onBackPressed()

        GlobalScope.launch {
            if (binding.addToWishlistBtn.isChecked) {
                if (!favoriteRecipeDatabase.favoriteRecipeDao().isRecipeExists(real_Id)) {
                    favoriteRecipeDatabase.favoriteRecipeDao()
                        .insertRecipeDetails(FavoriteRecipeModel(recipeInformation.id, recipeInformation.title, recipeInformation.image))
                }
            } else {
                favoriteRecipeDatabase.favoriteRecipeDao()
                    .deleteRecipeDetails(FavoriteRecipeModel(recipeInformation.id, recipeInformation.title, recipeInformation.image))
            }
        }
    }



}