package com.example.reciipiie.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipetable")
data class FavoriteRecipeModel(
    @PrimaryKey(autoGenerate = true)
    var recipeId:Int,
    var recipeTitle:String,
    var recipeImage: String,

)
