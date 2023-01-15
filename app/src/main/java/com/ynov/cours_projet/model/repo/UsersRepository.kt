package com.ynov.cours_projet.model.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UsersRepository {
    private var auth: FirebaseAuth = Firebase.auth

    fun getUserAuth(): FirebaseUser? {
        return auth.currentUser
    }
    suspend fun createUser (email: String, password: String): Boolean {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user != null
    }
    companion object {
        const val TAG = "UserRepository"
    }
}