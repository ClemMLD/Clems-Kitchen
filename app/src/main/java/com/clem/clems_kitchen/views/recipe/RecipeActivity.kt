package com.clem.clems_kitchen.views.recipe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityRecipeBinding
import com.clem.clems_kitchen.utils.GoBack
import com.clem.clems_kitchen.viewmodels.recipe.RecipeViewModel

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
        viewModel.setCurrentActivity(this)

        if (recipeId != null) {
            viewModel.showRecipe(recipeId)
        }

        this.viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val recipeToolbar: Toolbar = findViewById(R.id.recipeToolbar)
        recipeToolbar.setOnClickListener {
            GoBack().goBack(this)
        }

        val favoriteButton: Button = findViewById(R.id.recipeFavoriteButton)
        favoriteButton.setOnClickListener {
            viewModel.addRecipeToFavorites()
        }

        val shareButton : Button = findViewById(R.id.recipeShareButton)
        shareButton.setOnClickListener {
            viewModel.shareRecipe()
        }
    }
}