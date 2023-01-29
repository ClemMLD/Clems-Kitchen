package com.ynov.cours_projet.model

data class RecipeHomeListResponse(
    val results: List<RecipeHomeResponse>,
    val offset: Int?,
    val number: Int?,
    val totalResults: Int?
)