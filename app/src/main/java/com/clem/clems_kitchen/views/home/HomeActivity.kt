package com.clem.clems_kitchen.views.home

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityHomeBinding
import com.clem.clems_kitchen.viewmodels.home.HomeViewModel
import com.clem.clems_kitchen.views.account.AccountActivity
import com.clem.clems_kitchen.views.account.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_home)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home
        )

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.setCurrentActivity(this)

        viewModel.navigateToAccountEvent.observe(this) {
            if (it == true) {
                val intent = Intent(this@HomeActivity, AccountActivity::class.java)
                startActivity(intent)
                viewModel.navigateToAccountEvent.value = false
            }
        }

        viewModel.navigateToLoginEvent.observe(this) {
            if (it == true) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                viewModel.navigateToLoginEvent.value = false
            }
        }

        val recipeRecyclerView = findViewById<RecyclerView>(R.id.homeRecyclerView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)

        val searchBar = findViewById<EditText>(R.id.homeSearchEditText)
        searchBar.setOnEditorActionListener { v, _, _ ->
                val searchTerm = v.text.toString()
                viewModel.getRecipes(searchTerm, recipeRecyclerView)
                true

        }

        val userButton: FloatingActionButton = findViewById(R.id.homeAccountFloatingButton)
        userButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                viewModel.navigateToAccount()
            } else {
                viewModel.navigateToLogin()
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}