package com.clem.clems_kitchen.viewmodels.home

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.model.api.RecipeSearchApiCall
import com.clem.clems_kitchen.views.home.RecipeHomeAdapter
import com.clem.clems_kitchen.model.response.RecipeSearchListResponse
import com.clem.clems_kitchen.views.home.HomeActivity
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.ref.WeakReference


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var currentActivityRef: WeakReference<Activity>? = null
    val navigateToAccountEvent = MutableLiveData<Boolean>()
    val navigateToLoginEvent = MutableLiveData<Boolean>()
    private val interceptor = Interceptor { chain ->
        val request = chain.request()
        request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        val response = chain.proceed(request)
        response.cacheResponse()
        response
    }

    private val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
    private val httpCacheDirectory = File(application.cacheDir, "http-cache")
    private val cache = Cache(httpCacheDirectory, cacheSize)

    private val recipeSearchApiCall = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build())
        .build()
        .create(RecipeSearchApiCall::class.java)

    fun setCurrentActivity(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    fun getRecipes(query: String, recipeRecyclerView: RecyclerView) {
        currentActivityRef?.get()?.let { activity ->
            val apiKey = activity.getString(R.string.api_key)
            val number = 30
            val call = recipeSearchApiCall.getRecipes(apiKey, number, query)
            call.enqueue(object : Callback<RecipeSearchListResponse> {
                override fun onResponse(
                    call: Call<RecipeSearchListResponse>,
                    response: Response<RecipeSearchListResponse>
                ) {
                    if (response.isSuccessful) {
                        val recipes = response.body()?.results
                        val adapter = recipes?.let { RecipeHomeAdapter(it, activity as HomeActivity) }
                        recipeRecyclerView.adapter = adapter
                        if (recipes != null) {
                            for (recipe in recipes) {
                                if (adapter != null) {
                                    adapter.recipes = recipes
                                }
                                val position = adapter?.itemCount ?: 0
                                adapter?.notifyItemInserted(position)
                            }
                        }
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.response_not_successful) , Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RecipeSearchListResponse>, t: Throwable) {
                }
            })
        }
    }

    fun navigateToAccount() {
        navigateToAccountEvent.value = true
    }
    fun navigateToLogin() {
        navigateToLoginEvent.value = true
    }
}