package com.clem.clems_kitchen.viewmodels.account

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.utils.GoBack
import com.clem.clems_kitchen.views.account.AccountActivity
import com.clem.clems_kitchen.views.account.AccountRecipeAdapter

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val mAuth = FirebaseAuth.getInstance()

    fun displayFavorites(favoriteRecyclerView: RecyclerView, context: AccountActivity) {
        val user = FirebaseAuth.getInstance().currentUser
        val database = Firebase.firestore
        val noFavoriteTextView = context.findViewById<TextView>(R.id.accountNoFavoriteTextView)
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
                            val position = adapter?.itemCount ?: 0
                            adapter?.notifyItemInserted(position)
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

    fun disconnectAndGoBack(context: AccountActivity) {
        mAuth.signOut()
        GoBack().goBack(context)
    }
}