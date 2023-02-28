package com.clem.clems_kitchen.viewmodels.recipe

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Intent
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.model.api.RecipeInformationApiCall
import com.clem.clems_kitchen.model.response.RecipeInformationListResponse
import com.clem.clems_kitchen.views.recipe.RecipeActivity
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val id = MutableLiveData<String>()
    val interceptor = Interceptor { chain ->
        val request = chain.request()
        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        val response = chain.proceed(request)
        response.cacheResponse()
        response
    }

    val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
    val httpCacheDirectory = File(application.cacheDir, "http-cache")
    val cache = Cache(httpCacheDirectory, cacheSize)

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String>()

    private val RecipeInformationApiCall = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build())
        .build()
        .create(RecipeInformationApiCall::class.java)

    fun showRecipe(recipeId: String, context: RecipeActivity) {
        id.value = recipeId
        val apiKey = "1557583705024ba397186c1bfac970d2"
        val call = RecipeInformationApiCall.getRecipes(recipeId, apiKey)
        call.enqueue(object : Callback<RecipeInformationListResponse> {
            override fun onResponse(call: Call<RecipeInformationListResponse>, response: Response<RecipeInformationListResponse>) {
                val recipeTitle = context.findViewById<TextView>(R.id.recipeTitle)
                val recipePrepMinutes = context.findViewById<TextView>(R.id.recipePrepMinutesTextView)
                val recipeImage = context.findViewById<ImageView>(R.id.recipePictureImageView)
                val ingredientsTextView = context.findViewById<TextView>(R.id.recipeIngredientsTextView)
                val ingredients = response.body()?.extendedIngredients
                val summaryTextView = context.findViewById<TextView>(R.id.recipeSummaryTextView)
                val sourceUrlTextView = context.findViewById<TextView>(R.id.recipeSourceUrlTextView)
                val foodTypeTextView = context.findViewById<TextView>(R.id.recipeFoodTypeTextView)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (ingredients != null) {
                            for (ingredient in ingredients) {
                                ingredientsTextView.append(ingredient.name + ", ")
                            }
                            ingredientsTextView.text = ingredientsTextView.text.substring(0, ingredientsTextView.text.lastIndexOf(", "))
                        }
                        recipeTitle.text = response.body()?.title
                        title.value = response.body()?.title
                        recipePrepMinutes.text = response.body()?.readyInMinutes.toString()
                        Picasso.get().load(response.body()?.image).into(recipeImage)
                        summaryTextView.text = response.body()?.summary?.let { Jsoup.parse(it).text() }
                        description.value = response.body()?.summary?.let { Jsoup.parse(it).text() }
                        sourceUrlTextView.text = Html.fromHtml("Source : <a href='${response.body()?.spoonacularSourceUrl}'>${response.body()?.spoonacularSourceUrl}</a>")
                        sourceUrlTextView.movementMethod = LinkMovementMethod.getInstance()
                        if (response.body()?.vegetarian == true && response.body()?.vegan == true) {
                            foodTypeTextView.text = "Vegeratian, Vegan"
                        }
                        else if (response.body()?.vegetarian == true) {
                            foodTypeTextView.text = "Vegeratian"
                        }
                        else if (response.body()?.vegan == true) {
                            foodTypeTextView.text = "Vegan"
                        }
                    }
                } else {
                    recipeTitle.text = "Error, please come back later."
                }
            }
            override fun onFailure(call: Call<RecipeInformationListResponse>, t: Throwable) {
            }
        })
    }
    fun addRecipeToFavorites(context: RecipeActivity) {
        // Check if user is logged in
        val user =  FirebaseAuth.getInstance().currentUser
        val title = context.findViewById<TextView>(R.id.recipeTitle).text
        if (user != null) {
            // Add recipe to user's favorites
            val database = Firebase.firestore
            val recipeData = hashMapOf(
                "id" to id.value,
                "title" to title
            )
            database.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(id.value.toString())
                .set(recipeData)
                .addOnSuccessListener {
                    Log.d(TAG, "Recipe added to favorites")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding recipe to favorites", e)
                }
        }
    }

    fun shareRecipe(recipeActivity: RecipeActivity) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this recipe : ${title.value} ! ${description.value}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(recipeActivity, shareIntent, null)
    }

    fun goBack(context: RecipeActivity) {
        context.finish();
    }
}