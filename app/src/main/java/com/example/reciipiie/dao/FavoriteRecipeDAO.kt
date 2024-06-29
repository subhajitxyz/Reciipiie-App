package com.example.reciipiie.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.reciipiie.models.FavoriteRecipeModel


@Dao
interface FavoriteRecipeDAO {
    @Insert
    suspend fun insertRecipeDetails(model: FavoriteRecipeModel)
    @Update
    suspend fun updateRecipeDetails(model:FavoriteRecipeModel)
    @Delete
    suspend fun deleteRecipeDetails(model: FavoriteRecipeModel)
    @Query("SELECT * FROM recipetable")
    fun getAllRecipeDetails(): LiveData<List<FavoriteRecipeModel>>

    @Query("SELECT EXISTS(SELECT 1 FROM recipetable WHERE recipeId = :id)")
    fun isRecipeExists(id: Int): Boolean

}