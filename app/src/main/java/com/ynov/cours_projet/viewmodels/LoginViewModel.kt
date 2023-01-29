package com.ynov.cours_projet.viewmodels

import android.R
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var currentActivity: Activity
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val firebaseAuth = FirebaseAuth.getInstance()
    val navigateToRegisterEvent = MutableLiveData<Boolean>()

    fun navigateToRegister() {
        navigateToRegisterEvent.value = true
    }

    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _toastMessage.value = "Please provide account informations"
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _toastMessage.value = "Login successful"
                    currentActivity.finish()
                } else {
                    _toastMessage.value = "Login failed"
                }
            }
        }
    }
}