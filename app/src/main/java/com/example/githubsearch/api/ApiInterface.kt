package com.example.githubsearch.api

import com.example.githubsearch.data.model.SearchDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("/search/users")
    suspend fun fetchGithubUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): SearchDataResponse

    @GET("users/{Id}")
    suspend fun fetchUserDetail(@Path("Id") login: String): Response<SearchDataResponse.Items>
}