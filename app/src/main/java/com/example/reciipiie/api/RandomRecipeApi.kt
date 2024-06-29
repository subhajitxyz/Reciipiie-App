package com.example.reciipiie.api

import com.example.reciipiie.models.RandomRecipeModel
import com.example.reciipiie.models.Recipe
import com.example.reciipiie.models.SearchRecipeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RandomRecipeApi{
    @GET("recipes/random")
    suspend fun getProducts(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int
    ): Response<RandomRecipeModel>



    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Response<Recipe>


    @GET("recipes/complexSearch")
    suspend fun getSearchedProducts(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int
    ): Response<SearchRecipeModel>



}