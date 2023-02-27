package com.ynov.cours_projet.viewmodels

import android.app.Activity
import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ynov.cours_projet.R
import com.ynov.cours_projet.model.*
import com.ynov.cours_projet.views.AccountActivity

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val mAuth = FirebaseAuth.getInstance()
    lateinit var currentActivity: Activity

    fun displayFavorites(favoriteRecyclerView: RecyclerView, context: AccountActivity) {
        val user = FirebaseAuth.getInstance().currentUser
        val database = Firebase.firestore
        val noFavoriteTextView = context.findViewById<TextView>(R.id.noFavoriteTextView)
        if (user != null) {
            database.collection("users")
                .document(user.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    val adapter = result?.let { AccountRecipeAdapter(it, context) }
                    favoriteRecyclerView.adapter = adapter
                    if (result != null) {
                        for (document in result) {
                            if (adapter != null) {
                                adapter.recipeList = result
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                        if (result.isEmpty) {
                            noFavoriteTextView.text = context.getString(R.string.no_favorite)
                        }
                    }
                }
        }
    }

    fun removeFavorite(id: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val database = Firebase.firestore
        if (user != null) {
            database.collection("users")
                .document(user.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        database.collection("users")
                            .document(user.uid)
                            .collection("favorites")
                            .document(id)
                            .delete()
                    }
                }
        }
    }

    fun disconnectAndGoBack() {
        mAuth.signOut()
        currentActivity.finish()
    }
}