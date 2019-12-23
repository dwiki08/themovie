package com.dice.themovie.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val API_URL = "https://api.themoviedb.org/3/"
    const val POSTER_URL = "https://image.tmdb.org/t/p/w185/"
    const val BACKDROP_URL = "https://image.tmdb.org/t/p/w500/"
    const val API_KEY = "162654f57560142d89270459e4e2e844"

    fun createService(): ApiServices {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_URL)
            .build()
            .create(ApiServices::class.java)
    }
}