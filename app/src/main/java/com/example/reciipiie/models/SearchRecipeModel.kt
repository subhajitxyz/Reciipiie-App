package com.example.reciipiie.models

data class SearchRecipeModel(
    val number: Int,
    val offset: Int,
    val results: List<SearchModel>,
    val totalResults: Int
)