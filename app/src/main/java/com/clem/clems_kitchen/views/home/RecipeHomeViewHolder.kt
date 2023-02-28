package com.clem.clems_kitchen.views.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.model.response.RecipeSearchResponse

class RecipeHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recipeTitle = itemView.findViewById<TextView>(R.id.recipeTitle)

    fun bind(recipe: RecipeSearchResponse) {
        recipeTitle.text = recipe.title
    }
}