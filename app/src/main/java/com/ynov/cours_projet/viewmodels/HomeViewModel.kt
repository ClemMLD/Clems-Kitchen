package com.ynov.cours_projet.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.ynov.cours_projet.model.RecipeAPI
import com.ynov.cours_projet.model.RecipeHomeAdapter
import com.ynov.cours_projet.model.RecipeHomeListResponse
import com.ynov.cours_projet.views.HomeActivity
import com.ynov.cours_projet.views.RecipeActivity
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class HomeViewModel(application: Application) : AndroidViewModel(application) {
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

    private val recipeAPI = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build())
        .build()
        .create(RecipeAPI::class.java)

    fun getRecipes(query: String, recipeRecyclerView: RecyclerView, context: HomeActivity) {
        val apiKey = "1557583705024ba397186c1bfac970d2"
        val number = 30
        val call = recipeAPI.getRecipes(apiKey, number, query)
        call.enqueue(object : Callback<RecipeHomeListResponse> {
            override fun onResponse(call: Call<RecipeHomeListResponse>, response: Response<RecipeHomeListResponse>) {
                if (response.isSuccessful) {
                    val recipes = response.body()?.results
                    val adapter = recipes?.let { RecipeHomeAdapter(it, context) }
                    recipeRecyclerView.adapter = adapter
                    if (recipes != null) {
                        for (recipe in recipes) {
                            if (adapter != null) {
                                adapter.recipes = recipes
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<RecipeHomeListResponse>, t: Throwable) {
            }
        })
    }

    fun navigateToRecipes(context: Context, recipeViewModel: RecipeViewModel) {
        val intent = Intent(context, RecipeActivity::class.java)
        context.startActivity(intent)
    }
}