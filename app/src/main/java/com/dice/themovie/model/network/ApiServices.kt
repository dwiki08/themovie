package com.dice.themovie.model.network

import com.dice.themovie.model.Movie
import com.dice.themovie.model.MovieList
import com.dice.themovie.model.TvShow
import com.dice.themovie.model.TvShowList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("discover/movie")
    fun getMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieList>

    @GET("discover/tv")
    fun getTvShow(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<TvShowList>

    @GET("search/movie")
    fun getMovie(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("query") query: String?
    ): Call<MovieList>

    @GET("search/tv")
    fun getTvShow(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("query") query: String?
    ): Call<TvShowList>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<Movie>

    @GET("tv/{tv_id}")
    fun getTvShowDetail(
        @Path("tv_id") movie_id: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<TvShow>

    @GET("discover/movie")
    fun getReleaseMovie(
        @Query("api_key") api_key: String,
        @Query("primary_release_date.gte") start_release_date: String,
        @Query("primary_release_date.lte") until_release_date: String
    ): Call<MovieList>

}