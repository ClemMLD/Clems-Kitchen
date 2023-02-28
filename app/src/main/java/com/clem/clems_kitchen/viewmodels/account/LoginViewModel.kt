package com.clem.clems_kitchen.viewmodels.account

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clem.clems_kitchen.utils.GoBack
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var currentActivityRef: WeakReference<Activity>? = null
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val firebaseAuth = FirebaseAuth.getInstance()
    val navigateToRegisterEvent = MutableLiveData<Boolean>()

    fun setCurrentActivity(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    fun signIn(email: String, password: String) {
        currentActivityRef?.get()?.let { activity ->
            if (email.isEmpty() || password.isEmpty()) {
                _toastMessage.value = "Please provide account informations"
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _toastMessage.value = "Login successful"
                        GoBack().goBack(activity)
                    } else {
                        _toastMessage.value = "Login failed"
                    }
                }
            }
        }
    }

    fun navigateToRegister() {
        navigateToRegisterEvent.value = true
    }
}