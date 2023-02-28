package com.clem.clems_kitchen.model.response

data class RecipeInformationListResponse(
    val extendedIngredients: List<RecipeInformationIngredientsResponse>,
    val title: String?,
    val image: String?,
    val readyInMinutes: Int?,
    val summary: String?,
    val spoonacularSourceUrl: String?,
    val vegetarian: Boolean,
    val vegan: Boolean
)