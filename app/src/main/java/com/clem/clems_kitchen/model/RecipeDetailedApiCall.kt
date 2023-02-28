package com.clem.clems_kitchen.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeDetailedAPI {
    @GET("recipes/{query}/information")
    fun getRecipes(
        @Path("query") query: String,
        @Query("apiKey") apiKey: String,
    ): Call<RecipeDetailedListResponse>
}