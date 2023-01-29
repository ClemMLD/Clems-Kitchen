package com.ynov.cours_projet.viewmodels

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var currentActivity: Activity
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val firebaseAuth = FirebaseAuth.getInstance()
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordConfirmation = MutableLiveData<String>("")

    fun signUp(email: String, password: String, passwordConfirmation: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _toastMessage.value = "Please provide account informations"
        } else if (password != passwordConfirmation) {
            _toastMessage.value = "The passwords don't match"
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _toastMessage.value = "Sign up successful"
                        currentActivity.finish()
                    } else {
                        _toastMessage.value = "Sign up failed"
                    }
                }
        }

    }
}