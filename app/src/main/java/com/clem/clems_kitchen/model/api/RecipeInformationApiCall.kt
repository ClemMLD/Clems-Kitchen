package com.clem.clems_kitchen.model.api

import com.clem.clems_kitchen.model.response.RecipeInformationListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeInformationApiCall {
    @GET("recipes/{query}/information")
    fun getRecipes(
        @Path("query") query: String,
        @Query("apiKey") apiKey: String,
    ): Call<RecipeInformationListResponse>
}