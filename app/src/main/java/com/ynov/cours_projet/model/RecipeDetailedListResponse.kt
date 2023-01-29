package com.ynov.cours_projet.model

data class RecipeDetailedListResponse(
    val extendedIngredients: List<RecipeDetailedIngredientsResponse>,
    val title: String?,
    val image: String?,
    val preparationMinutes: Int?,
    val summary: String?,
)