package com.example.capstone.api

import com.example.capstone.model.SSRResult
import retrofit2.http.GET
import retrofit2.http.Query


interface SSRApiService {

    // The GET method needed to retrieve a random number trivia.
    @GET("personal_records.php?")
    suspend fun getSSRPersonalRecord(@Query("skater")skaterId: Int): SSRResult
}