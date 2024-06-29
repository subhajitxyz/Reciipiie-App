package com.example.reciipiie.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciipiie.models.RandomRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchRecipeModel
import com.example.reciipiie.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RecipeRepository): ViewModel() {



    init{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getProducts()

        }
    }

    val products : LiveData<RandomRecipeModel>
        get() = repository.products

    val recipe: LiveData<Recipe> get()=repository.recipeDetails

   fun fetchRecipeDetails(id:Int){
       viewModelScope.launch (Dispatchers.IO){
          repository.getRecipeDetails(id)
       }
   }
    val searchRecipe: LiveData<SearchRecipeModel> get()=repository.searchRecipeDetails

    fun fetchSearchRecipeDetails(query:String,id:Int){
        viewModelScope.launch (Dispatchers.IO){
            repository.getSearchRecipeDetails(query,id)
        }
    }
}