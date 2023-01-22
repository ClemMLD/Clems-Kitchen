package com.ynov.cours_projet.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.ynov.cours_projet.R
import com.ynov.cours_projet.databinding.ActivityLoginBinding
import com.ynov.cours_projet.databinding.ActivityRegisterBinding
import com.ynov.cours_projet.viewmodels.LoginViewModel
import com.ynov.cours_projet.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity()  {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val binding: ActivityRegisterBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_register
        )
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    fun goBack(view: View) {
        finish();
    }
}