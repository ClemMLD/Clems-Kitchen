package com.clem.clems_kitchen.views.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityLoginBinding
import com.clem.clems_kitchen.utils.GoBack
import com.clem.clems_kitchen.viewmodels.account.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_login
        )

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.navigateToRegisterEvent.observe(this) {
            if (it == true) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                viewModel.navigateToRegisterEvent.value = false
            }
        }
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.setCurrentActivity(this)

        val loginToolbar: Toolbar = findViewById(R.id.loginToolbar)
        loginToolbar.setOnClickListener {
            GoBack().goBack(this)
        }

        val registerButton: Button = findViewById(R.id.loginRegisterButton)
        registerButton.setOnClickListener {
            viewModel.navigateToRegister()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}