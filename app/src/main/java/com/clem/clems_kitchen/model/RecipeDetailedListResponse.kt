package com.clem.clems_kitchen.model

data class RecipeDetailedListResponse(
    val extendedIngredients: List<RecipeDetailedIngredientsResponse>,
    val title: String?,
    val image: String?,
    val readyInMinutes: Int?,
    val summary: String?,
    val spoonacularSourceUrl: String?,
    val vegetarian: Boolean,
    val vegan: Boolean
)