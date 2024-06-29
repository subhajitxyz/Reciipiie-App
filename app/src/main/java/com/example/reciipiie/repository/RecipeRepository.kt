package com.example.reciipiie.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.reciipiie.api.RandomRecipeApi
import com.example.reciipiie.models.RandomRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchRecipeModel
import kotlin.random.Random


class RecipeRepository (private val randomRecipeApi:RandomRecipeApi) {
    private val API_KEY = "b0e146db75cd4e2389f88c977dc71d49"

    private val randomRecipeLivedata = MutableLiveData<RandomRecipeModel>()
    val products : LiveData<RandomRecipeModel>
        get()= randomRecipeLivedata



    suspend fun getProducts(){
        val result =randomRecipeApi.getProducts(API_KEY,20)
        if(result?.body()!=null){
            randomRecipeLivedata.postValue(result.body())
        }
    }


    private val recipeDetailsLiveData = MutableLiveData<Recipe>()
    val recipeDetails: LiveData<Recipe>
        get()  = recipeDetailsLiveData

    suspend fun getRecipeDetails(id:Int){
        val result = randomRecipeApi.getRecipeDetails(id,API_KEY)

        if(result?.body()!=null){
            recipeDetailsLiveData.postValue(result.body())
        }else{
            Log.d("Subha","error")
        }
    }

    private val searchListLiveData = MutableLiveData<SearchRecipeModel>()
    val searchRecipeDetails: LiveData<SearchRecipeModel>
        get()  = searchListLiveData

    suspend fun getSearchRecipeDetails(query:String,id:Int){
        val result = randomRecipeApi.getSearchedProducts(API_KEY,query,id)

        if(result?.body()!=null){
            searchListLiveData.postValue(result.body())
        }else{
            Log.d("Subha","error")
        }
    }


}
