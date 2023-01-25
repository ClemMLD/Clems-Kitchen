package com.ynov.cours_projet.viewmodels

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.ynov.cours_projet.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    val recipes = MutableLiveData<List<Recipe>>()

    fun fetchRecipes() {
        db.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = mutableListOf<Recipe>()
                for (document in querySnapshot) {
                    val recipe = Recipe(
                        document.getString("name"),
                        document.get("ingredients") as List<String>,
                        document.getString("instructions"),
                        document.getString("imageUrl"),
                        document.getLong("prepTime"),
                        document.getLong("cookTime"),
                        document.getLong("servings"),
                        document.getString("difficulty"),
                        document.getString("category")
                    )
                    recipes.add(recipe)
                }
                this.recipes.value = recipes
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}