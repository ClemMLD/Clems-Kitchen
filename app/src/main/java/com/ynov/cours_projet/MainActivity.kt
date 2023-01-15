package com.ynov.cours_projet

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ynov.cours_projet.databinding.ActivityMainBinding
import com.ynov.cours_projet.databinding.RegisterPageBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    fun onClickRegisterButton(view: View) {
        createUserAccount()
    }

    fun onClickLoginButton(view: View) {
        loginUser()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        if(currentUser != null){
            reload();
        }
    }

    private fun reload() {
        TODO("Not yet implemented")
    }

    private fun loginUser(): Boolean {

        var email = findViewById<EditText>(R.id.loginEmailField)
        var password = findViewById<EditText>(R.id.loginPasswordField)

        if(TextUtils.isEmpty(email.text.toString())) {
            Toast.makeText(
                baseContext, "Please enter your email address",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if(TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(
                baseContext, "Please enter your password",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
        return true
    }

    private fun createUserAccount(): Boolean {

        var email = findViewById<EditText>(R.id.registrationEmailField)
        var password = findViewById<EditText>(R.id.registrationPasswordField)

        if(TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(
                    baseContext, "Please enter your email address",
                    Toast.LENGTH_SHORT
                ).show()
                return false
        }

        if(TextUtils.isEmpty(password.text.toString())) {
            Toast.makeText(
                baseContext, "Please enter your password",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
        return true
    }

    fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}