package com.clem.clems_kitchen.views.account

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.databinding.ActivityAccountBinding
import com.clem.clems_kitchen.utils.GoBack
import com.clem.clems_kitchen.viewmodels.account.AccountViewModel

class AccountActivity : AppCompatActivity()  {
    lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        val binding: ActivityAccountBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_account
        )

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        val disconnectButton = findViewById<Button>(R.id.accountDisconnectButton)
        disconnectButton.setOnClickListener {
            viewModel.disconnectAndGoBack(this)
        }

        val loginToolbar: Toolbar = findViewById(R.id.accountToolbar)
        loginToolbar.setOnClickListener {
            GoBack().goBack(this)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val favoriteRecyclerView = findViewById<RecyclerView>(R.id.accountRecyclerView)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.displayFavorites(favoriteRecyclerView, this)
    }
}