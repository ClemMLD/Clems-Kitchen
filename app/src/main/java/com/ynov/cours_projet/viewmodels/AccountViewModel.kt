package com.ynov.cours_projet.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.ynov.cours_projet.views.HomeActivity

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val mAuth = FirebaseAuth.getInstance()
    lateinit var currentActivity: Activity

    fun disconnectAndGoBack() {
        mAuth.signOut()
        currentActivity.finish()
    }
}