package com.ynov.cours_projet.model

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ynov.cours_projet.R
import com.ynov.cours_projet.views.HomeActivity
import com.ynov.cours_projet.views.RecipeActivity

class RecipeHomeAdapter(var recipes: List<RecipeHomeResponse>, private val context: HomeActivity) : RecyclerView.Adapter<RecipeHomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_view, parent, false)
        return RecipeHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeHomeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.recipeTitle.text = recipe.title
        holder.recipeTitle.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("recipeId", recipe.id.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}