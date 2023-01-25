package com.ynov.cours_projet.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityAccountBinding
import com.ynov.cours_projet.databinding.ActivityRecipeBinding
import com.ynov.cours_projet.viewmodels.AccountViewModel
import com.ynov.cours_projet.viewmodels.RecipeViewModel

class RecipeActivity : AppCompatActivity() {
    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val binding: ActivityRecipeBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_recipe
        )

        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}