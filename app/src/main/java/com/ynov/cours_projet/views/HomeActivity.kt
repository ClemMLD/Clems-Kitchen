package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityHomeBinding
import com.ynov.cours_projet.viewmodels.HomeViewModel
import com.ynov.cours_projet.viewmodels.RecipeViewModel


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_home
        )

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.navigateToAccountEvent.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this@HomeActivity, AccountActivity::class.java)
                startActivity(intent)
                viewModel.navigateToAccountEvent.value = false
            }
        })

        viewModel.navigateToLoginEvent.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this@HomeActivity, LoginActivity::class.java)
                startActivity(intent)
                viewModel.navigateToLoginEvent.value = false
            }
        })

        val recipeRecyclerView = findViewById<RecyclerView>(R.id.recipe_recycler_view)
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)

        val searchBar = findViewById<EditText>(R.id.recipeSearchEditText)
        searchBar.setOnEditorActionListener { v, actionId, _ ->
                // get the text from the searchbar
                val searchTerm = v.text.toString()
                // call the getRecipes() function with the searchTerm as a parameter
                viewModel.getRecipes(searchTerm, recipeRecyclerView, this)
                true

        }

        val userButton: FloatingActionButton = findViewById(R.id.userButton)
        userButton.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null) {
                viewModel.navigateToAccount();
            } else {
                viewModel.navigateToLogin();
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}