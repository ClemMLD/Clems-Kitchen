package com.clem.clems_kitchen.viewmodels.account

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clem.clems_kitchen.utils.GoBack
import com.google.firebase.auth.FirebaseAuth
import java.lang.ref.WeakReference

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private var currentActivityRef: WeakReference<Activity>? = null
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage
    private val firebaseAuth = FirebaseAuth.getInstance()
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordConfirmation = MutableLiveData("")

    fun setCurrentActivity(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    fun signUp(email: String, password: String, passwordConfirmation: String) {
        currentActivityRef?.get()?.let { activity ->
            if (email.isEmpty() || password.isEmpty()) {
                _toastMessage.value = "Please provide account informations"
            } else if (password != passwordConfirmation) {
                _toastMessage.value = "The passwords don't match"
            } else {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _toastMessage.value = "Sign up successful"
                            GoBack().goBack(activity)
                        } else {
                            _toastMessage.value = "Sign up failed"
                        }
                    }
            }
        }
    }
}