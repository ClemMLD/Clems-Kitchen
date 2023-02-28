package com.clem.clems_kitchen.viewmodels.recipe

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.clem.clems_kitchen.R
import com.clem.clems_kitchen.model.api.RecipeInformationApiCall
import com.clem.clems_kitchen.model.response.RecipeInformationListResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
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
import java.lang.ref.WeakReference

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private var currentActivityRef: WeakReference<Activity>? = null
    private val id = MutableLiveData<String>()
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

    val title = MutableLiveData<String?>()
    val description = MutableLiveData<String>()

    private val recipeInformationApiCall = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build())
        .build()
        .create(RecipeInformationApiCall::class.java)

    fun setCurrentActivity(activity: Activity) {
        currentActivityRef = WeakReference(activity)
    }

    fun showRecipe(recipeId: String) {
        currentActivityRef?.get()?.let { activity ->
            id.value = recipeId
            val apiKey = activity.getString(R.string.api_key)
            val call = recipeInformationApiCall.getRecipes(recipeId, apiKey)
            call.enqueue(object : Callback<RecipeInformationListResponse> {
                override fun onResponse(
                    call: Call<RecipeInformationListResponse>,
                    response: Response<RecipeInformationListResponse>
                ) {
                    val recipeTitle = activity.findViewById<TextView>(R.id.recipeTitle)
                    val recipePrepMinutes =
                        activity.findViewById<TextView>(R.id.recipePrepMinutesTextView)
                    val recipeImage = activity.findViewById<ImageView>(R.id.recipePictureImageView)
                    val ingredientsTextView =
                        activity.findViewById<TextView>(R.id.recipeIngredientsTextView)
                    val ingredients = response.body()?.extendedIngredients
                    val summaryTextView =
                        activity.findViewById<TextView>(R.id.recipeSummaryTextView)
                    val sourceUrlTextView =
                        activity.findViewById<TextView>(R.id.recipeSourceUrlTextView)
                    val foodTypeTextView =
                        activity.findViewById<TextView>(R.id.recipeFoodTypeTextView)

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (ingredients != null) {
                                for (ingredient in ingredients) {
                                    ingredientsTextView.append(ingredient.name + ", ")
                                }
                                ingredientsTextView.text = ingredientsTextView.text.substring(
                                    0,
                                    ingredientsTextView.text.lastIndexOf(", ")
                                )
                            }
                            recipeTitle.text = response.body()?.title
                            title.value = response.body()?.title
                            recipePrepMinutes.text = response.body()?.readyInMinutes.toString()
                            Picasso.get().load(response.body()?.image).into(recipeImage)
                            summaryTextView.text =
                                response.body()?.summary?.let { Jsoup.parse(it).text() }
                            description.value =
                                response.body()?.summary?.let { Jsoup.parse(it).text() }
                            sourceUrlTextView.text = HtmlCompat.fromHtml(
                                "Source : <a href='${response.body()?.spoonacularSourceUrl}'>${response.body()?.spoonacularSourceUrl}</a>",
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                            sourceUrlTextView.movementMethod = LinkMovementMethod.getInstance()
                            if (response.body()?.vegetarian == true && response.body()?.vegan == true) {
                                foodTypeTextView.text = R.string.vegan_and_vegetarian.toString()
                            } else if (response.body()?.vegetarian == true) {
                                foodTypeTextView.text = R.string.vegetarian.toString()
                            } else if (response.body()?.vegan == true) {
                                foodTypeTextView.text = R.string.vegan.toString()
                            }
                        }
                    } else {
                        recipeTitle.text = R.string.generic_error_message.toString()
                    }
                }

                override fun onFailure(call: Call<RecipeInformationListResponse>, t: Throwable) {
                }
            })
        }
    }

    fun addRecipeToFavorites() {
        currentActivityRef?.get()?.let { activity ->
            val user = FirebaseAuth.getInstance().currentUser
            val title = activity.findViewById<TextView>(R.id.recipeTitle).text
            if (user != null) {
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
                database.collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(id.value.toString())
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.recipe_added_to_favorites),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.failed_to_add_recipe_to_favorites),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    fun shareRecipe() {
        currentActivityRef?.get()?.let { activity ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey, check out this recipe : ${title.value} ! ${description.value}"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(activity, shareIntent, null)
        }
    }
}