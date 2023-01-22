package com.ynov.cours_projet.viewmodels

import android.R
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val firebaseAuth = FirebaseAuth.getInstance()
    val navigateToLoginEvent = MutableLiveData<Boolean>()

    fun navigateToLogin() {
        navigateToLoginEvent.value = true
    }
}