package com.ynov.cours_projet.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityHomeBinding
import com.ynov.cours_projet.model.Recipe
import com.ynov.cours_projet.viewmodels.HomeViewModel
import com.ynov.cours_projet.viewmodels.RecipeViewModel


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        val db = FirebaseFirestore.getInstance()
        db.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val recipes = mutableListOf<Recipe>()
                for (document in querySnapshot) {
                    val recipe = Recipe(
                        document.getString("name"),
                        document.get("ingredients") as List<String>,
                        document.getString("instructions"),
                        document.getString("imageUrl"),
                        document.getLong("prepTime"),
                        document.getLong("cookTime"),
                        document.getLong("servings"),
                        document.getString("difficulty"),
                        document.getString("category")
                    )
                }
            }
        /*val recipe = hashMapOf(
            "name" to "Spaghetti Bolognese",
            "ingredients" to listOf("spaghetti", "ground beef", "tomato sauce", "onion", "garlic", "oregano"),
            "instructions" to "1. Cook spaghetti according to package instructions. 2. In a separate pan, brown ground beef over medium heat. 3. Add diced onion and minced garlic to the pan and cook until softened. 4. Stir in tomato sauce and oregano. 5. Simmer for 10 minutes. 6. Serve beef mixture over cooked spaghetti.",
            "imageUrl" to "https://www.example.com/spaghetti-bolognese.jpg",
            "prepTime" to 15,
            "cookTime" to 30,
            "servings" to 4,
            "difficulty" to "Easy",
            "category" to "Italian"
        )
        db.collection("recipes").add(recipe)*/
        setContentView(R.layout.activity_home)
        val goToRecipesButton: Button = findViewById(R.id.goToRecipesButton)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(
            this, com.ynov.cours_projet.R.layout.activity_home
        )
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val bottomNavigationView =
            findViewById<BottomNavigationView>(com.ynov.cours_projet.R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                com.ynov.cours_projet.R.id.action_inbox ->
                    return@OnNavigationItemSelectedListener true
                com.ynov.cours_projet.R.id.action_starred ->
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        startActivity(Intent(this, AccountActivity::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
            }
            false
        })
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToRecipeEvent.observe(this, Observer {
            if (it == true) {
                val intent = Intent(this@HomeActivity, RecipeActivity::class.java)
                startActivity(intent)
                viewModel.navigateToRecipeEvent.value = false
            }
        })

        goToRecipesButton.setOnClickListener {
            startActivity(Intent(this, RecipeActivity::class.java))
        }
    }
    fun goToRecipes(view: View) {
        startActivity(Intent(this, RecipeActivity::class.java))
    }
}