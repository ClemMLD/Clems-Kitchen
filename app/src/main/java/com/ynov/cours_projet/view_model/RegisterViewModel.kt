package com.ynov.cours_projet.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ynov.cours_projet.model.repo.UsersRepository

class RegisterViewModel : ViewModel() {
    var email: String = ""
    var password: String = ""
    var passwordConfirmation = ""
    private var users: UsersRepository = UsersRepository()

    fun getUsers(): UsersRepository {
        return users
    }

    suspend fun registerUser(): Boolean {
        if(email != "" && password != "" && password == passwordConfirmation) {
            Log.d("Register", "password = $password")
            users.createUser(email, password)
        } else {
            return false
        }
        Log.d("Register", "not_password = $password and $passwordConfirmation")
        return true
    }
    fun isEmail(): Boolean {
        return email != "" && email.length < 255
    }

    fun isPassword(): Boolean {
        return password != ""
    }
}