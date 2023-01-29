package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val RecipeviewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_inbox ->
                    return@OnNavigationItemSelectedListener true
                R.id.action_starred ->
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        startActivity(Intent(this, AccountActivity::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
            }
            false
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

        val goToRecipesButton = findViewById<Button>(R.id.goToRecipesButton)
        goToRecipesButton.setOnClickListener {
            viewModel.navigateToRecipes(this, RecipeviewModel)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}