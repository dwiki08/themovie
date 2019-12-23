package com.dice.themovie.model

data class MovieList(
    var page: String?,
    var total_results: String?,
    var total_pages: String?,
    var results: ArrayList<Movie>
)