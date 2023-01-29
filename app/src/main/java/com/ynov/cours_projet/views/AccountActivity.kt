package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityAccountBinding
import com.ynov.cours_projet.databinding.ActivityLoginBinding
import com.ynov.cours_projet.databinding.ActivityRegisterBinding
import com.ynov.cours_projet.viewmodels.AccountViewModel
import com.ynov.cours_projet.viewmodels.LoginViewModel
import com.ynov.cours_projet.viewmodels.RegisterViewModel

class AccountActivity : AppCompatActivity()  {
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val binding: ActivityAccountBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_account
        )

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        viewModel.currentActivity = this

        val disconnectButton = findViewById<Button>(R.id.disconnectButton)
        disconnectButton.setOnClickListener {
            viewModel.disconnectAndGoBack()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}