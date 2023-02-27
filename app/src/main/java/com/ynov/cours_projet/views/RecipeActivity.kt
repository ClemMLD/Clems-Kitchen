package com.ynov.cours_projet.views

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityRecipeBinding
import com.ynov.cours_projet.viewmodels.RecipeViewModel

class RecipeActivity : AppCompatActivity() {
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val binding: ActivityRecipeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_recipe
        )

        val recipeId = intent.getStringExtra("recipeId")

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        if (recipeId != null) {
            viewModel.showRecipe(recipeId, this)
        }

        this.viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val favoriteButton: Button = findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            viewModel.addRecipeToFavorites(this)
        }    }
}