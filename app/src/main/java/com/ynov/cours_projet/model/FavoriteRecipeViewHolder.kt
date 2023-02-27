package com.ynov.cours_projet.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ynov.cours_projet.R

class FavoriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recipeTitle = itemView.findViewById<TextView>(R.id.recipeTitle)

    fun bind(recipe: RecipeHomeResponse) {
        recipeTitle.text = recipe.title
    }
}