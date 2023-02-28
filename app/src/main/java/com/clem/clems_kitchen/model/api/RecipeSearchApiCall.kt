package com.clem.clems_kitchen.model.api

import com.clem.clems_kitchen.model.response.RecipeSearchListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeSearchApiCall {
    @GET("recipes/complexSearch")
    fun getRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int,
        @Query("query") query: String
    ): Call<RecipeSearchListResponse>
}