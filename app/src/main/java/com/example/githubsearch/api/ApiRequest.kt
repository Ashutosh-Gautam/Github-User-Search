package com.example.githubsearch.api

import retrofit2.Response

abstract class ApiRequest {
    private val networkErrorHandler = NetworkErrorHandler()

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        val response = call.invoke()
        return if (isSuccessResponse(response)) Result.Success(response.body()!!)
        else networkErrorHandler.resolveErrorMessage(response)
    }

    private fun <T> isSuccessResponse(response: Response<T>?): Boolean {
        return response != null && response.isSuccessful && response.body() != null
    }
}