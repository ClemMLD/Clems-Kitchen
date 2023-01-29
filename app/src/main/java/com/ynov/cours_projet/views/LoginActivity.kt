package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityLoginBinding
import com.ynov.cours_projet.viewmodels.LoginViewModel
import com.ynov.cours_projet.views.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login
        )

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.navigateToRegisterEvent.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                viewModel.navigateToRegisterEvent.value = false
            }
        })
        viewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
        viewModel.currentActivity = this

        val registerButton: Button = findViewById(R.id.registerPageButton)
        registerButton.setOnClickListener {
            viewModel.navigateToRegister()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}