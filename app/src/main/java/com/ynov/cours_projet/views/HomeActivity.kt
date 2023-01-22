package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityHomeBinding
import com.ynov.cours_projet.viewmodels.HomeViewModel
import com.ynov.cours_projet.views.RegisterActivity


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home
        )
        val loginPageButton: ImageButton = findViewById(R.id.loginPageButton)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.navigateToLoginEvent.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                viewModel.navigateToLoginEvent.value = false
            }
        })
        loginPageButton.setOnClickListener {
            viewModel.navigateToLogin()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}