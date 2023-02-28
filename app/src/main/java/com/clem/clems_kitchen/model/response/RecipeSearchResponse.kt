package com.clem.clems_kitchen.model.response

data class RecipeSearchResponse(
    var id: Int?,
    val title: String?,
    val image: String?,
    val imageType: String?
)