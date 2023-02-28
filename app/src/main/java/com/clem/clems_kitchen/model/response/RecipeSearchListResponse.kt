package com.clem.clems_kitchen.model.response

data class RecipeSearchListResponse(
    val results: List<RecipeSearchResponse>,
    val offset: Int?,
    val number: Int?,
    val totalResults: Int?
)