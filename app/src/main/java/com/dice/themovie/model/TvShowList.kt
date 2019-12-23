package com.dice.themovie.model

data class TvShowList(
    var page: String?,
    var total_results: String?,
    var total_pages: String?,
    var results: ArrayList<TvShow>
)