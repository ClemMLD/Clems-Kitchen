package com.ynov.cours_projet.viewmodels

import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ynov.cours_projet.R
import com.ynov.cours_projet.model.RecipeDetailedAPI
import com.ynov.cours_projet.model.RecipeDetailedListResponse
import com.ynov.cours_projet.model.RecipeHomeAdapter
import com.ynov.cours_projet.model.RecipeHomeResponse
import com.ynov.cours_projet.views.RecipeActivity
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

    private val RecipeDetailedAPI = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build())
        .build()
        .create(RecipeDetailedAPI::class.java)

    fun showRecipe(recipeId: String, context: RecipeActivity) {
        val apiKey = "1557583705024ba397186c1bfac970d2"
        val call = RecipeDetailedAPI.getRecipes(recipeId, apiKey)
        call.enqueue(object : Callback<RecipeDetailedListResponse> {
            override fun onResponse(call: Call<RecipeDetailedListResponse>, response: Response<RecipeDetailedListResponse>) {
                val recipeTitle = context.findViewById<TextView>(R.id.recipeTitle)
                val recipePrepMinutes = context.findViewById<TextView>(R.id.recipePrepMinutesTextView)
                val recipeImage = context.findViewById<ImageView>(R.id.recipeImageView)
                val ingredientsTextView = context.findViewById<TextView>(R.id.ingredientsTextView)
                val ingredients = response.body()?.extendedIngredients
                val summaryTextView = context.findViewById<TextView>(R.id.summaryTextView)

                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (ingredients != null) {
                            for (ingredient in ingredients) {
                                ingredientsTextView.append(ingredient.name + ", ")
                            }
                            ingredientsTextView.text = ingredientsTextView.text.substring(0, ingredientsTextView.text.lastIndexOf(", "))
                        }
                        recipeTitle.text = response.body()?.title
                        recipePrepMinutes.text = response.body()?.preparationMinutes.toString()
                        Picasso.get().load(response.body()?.image).into(recipeImage)
                        summaryTextView.text = Jsoup.parse(response.body()?.summary).text()
                    }
                } else {
                    recipeTitle.text = "Error, please come back later."
                }
            }
            override fun onFailure(call: Call<RecipeDetailedListResponse>, t: Throwable) {
            }
        })
    }
}