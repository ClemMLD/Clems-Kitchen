package com.clem.clems_kitchen.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityRegisterBinding
import com.clem.clems_kitchen.viewmodels.RegisterViewModel

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
        viewModel.currentActivity = this

        val registerToolbar: Toolbar = findViewById(R.id.registerToolbar)
        registerToolbar.setOnClickListener {
            viewModel.goBack()
        }

        viewModel.toastMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}