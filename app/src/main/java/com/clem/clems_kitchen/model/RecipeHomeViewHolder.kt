package com.clem.clems_kitchen.model

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clem.clems_kitchen.R

class RecipeHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recipeTitle = itemView.findViewById<TextView>(R.id.recipeTitle)

    fun bind(recipe: RecipeHomeResponse) {
        recipeTitle.text = recipe.title
    }
}