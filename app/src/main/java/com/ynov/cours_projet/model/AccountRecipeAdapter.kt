package com.ynov.cours_projet.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QuerySnapshot
import com.ynov.cours_projet.R
import com.ynov.cours_projet.views.AccountActivity

class AccountRecipeAdapter(
    var recipeList: QuerySnapshot,
    context: AccountActivity
) : RecyclerView.Adapter<AccountRecipeAdapter.FavoriteRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_view, parent, false)
        return FavoriteRecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteRecipeViewHolder, position: Int) {
        val currentRecipe = recipeList.documents[position]
        holder.titleTextView.text = currentRecipe.data?.get("title").toString()
    }

    override fun getItemCount() = recipeList.size()

    inner class FavoriteRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.recipeTitle)
    }
}