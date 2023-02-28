package com.clem.clems_kitchen.model

data class RecipeHomeListResponse(
    val results: List<RecipeHomeResponse>,
    val offset: Int?,
    val number: Int?,
    val totalResults: Int?
)