package com.ynov.cours_projet.model

data class Recipe(
    val name: String?,
    val ingredients: List<String?>,
    val instructions: String?,
    val imageUrl: String?,
    val prepTime: Long?,
    val cookTime: Long?,
    val servings: Long?,
    val difficulty: String?,
    val category: String?
)