package com.clem.clems_kitchen.views.account

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityRegisterBinding
import com.clem.clems_kitchen.utils.GoBack
import com.clem.clems_kitchen.viewmodels.account.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val binding: ActivityRegisterBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_register
        )
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewModel.setCurrentActivity(this)

        val registerToolbar: Toolbar = findViewById(R.id.registerToolbar)
        registerToolbar.setOnClickListener {
            GoBack().goBack(this)
        }

        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}