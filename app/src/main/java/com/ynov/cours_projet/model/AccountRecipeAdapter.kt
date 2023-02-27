package com.ynov.cours_projet.model

import android.content.Intent
import android.content.Intent.getIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.QuerySnapshot
import com.ynov.cours_projet.R
import com.ynov.cours_projet.views.AccountActivity
import com.ynov.cours_projet.views.RecipeActivity

class AccountRecipeAdapter(
    var recipeList: QuerySnapshot,
    private val context: AccountActivity
) : RecyclerView.Adapter<AccountRecipeAdapter.FavoriteRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favorite_recipe_item_view, parent, false)
        return FavoriteRecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        val currentRecipe = recipeList.documents[position]
        holder.recipeTitle.text = currentRecipe.data?.get("title").toString()
        holder.removeFavoriteButton.setOnClickListener {
            context.viewModel.removeFavorite(currentRecipe.data?.get("id").toString())
            Thread.sleep(500)
            context.finish();
            context.startActivity(context.intent);
        }
        holder.recipeTitle.setOnClickListener {
            val intent = Intent(context, RecipeActivity::class.java)
            intent.putExtra("recipeId", currentRecipe.data?.get("id").toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipeList.size()

    inner class FavoriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val removeFavoriteButton: FloatingActionButton = itemView.findViewById(R.id.removeFavoriteButton)
    }

}