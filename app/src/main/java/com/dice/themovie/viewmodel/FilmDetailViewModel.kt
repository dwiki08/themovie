package com.dice.themovie.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dice.themovie.R
import com.dice.themovie.model.Movie
import com.dice.themovie.model.TvShow
import com.dice.themovie.model.local.FavoriteDatabase
import com.dice.themovie.model.network.ApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmDetailViewModel(val context: Context, val id: String) : ViewModel() {
    companion object {
        private val TAG = FilmDetailViewModel::class.java.simpleName
    }

    private val apiService = ApiClient.createService()
    private val apiKey = ApiClient.API_KEY
    //private val language = context.getString(R.string.language) //not all items have translated language
    private val language = context.getString(R.string.default_language)

    private val favoriteDatabase = FavoriteDatabase.getInstance(context)

    private val movieDetail = MutableLiveData<Movie>()
    private val tvShowDetail = MutableLiveData<TvShow>()
    private var isFavorite = MutableLiveData<Boolean>()

    var isLoading = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()

    private fun setMovieDetail() {
        isLoading.postValue(true)

        apiService.getMovieDetail(id, apiKey, language).enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val movie = response.body()

                    movieDetail.postValue(movie)
                }
            }
        })
    }

    private fun setTvShowDetail() {
        isLoading.postValue(true)

        apiService.getTvShowDetail(id, apiKey, language).enqueue(object : Callback<TvShow> {
            override fun onFailure(call: Call<TvShow>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<TvShow>, response: Response<TvShow>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val tvShow = response.body()

                    tvShowDetail.postValue(tvShow)
                }
            }
        })
    }

    internal fun checkIsFavoriteMovie(id: String): LiveData<Boolean> {
        GlobalScope.launch {
            if (favoriteDatabase?.movieDao()?.getMovie(id) != null) {
                isFavorite.postValue(true)
            }
        }

        return isFavorite
    }

    internal fun checkIsFavoriteTvShow(id: String): LiveData<Boolean> {
        GlobalScope.launch {
            if (favoriteDatabase?.tvShowDao()?.getTvShow(id) != null){
                isFavorite.postValue(true)
            }
        }

        return isFavorite
    }

    internal fun setFavoriteMovie(movie: Movie) {
        GlobalScope.launch {
            favoriteDatabase?.movieDao()?.insertMovie(movie)
            isFavorite.postValue(true)
        }
    }

    internal fun setFavoriteTvShow(tvShow: TvShow) {
        GlobalScope.launch {
            favoriteDatabase?.tvShowDao()?.insertTvShow(tvShow)
            isFavorite.postValue(true)
        }
    }

    internal fun deleteFavoriteMovie(id: String){
        GlobalScope.launch {
            favoriteDatabase?.movieDao()?.deleteMovie(id)
            isFavorite.postValue(false)
        }
    }

    internal fun deleteFavoriteTvShow(id: String){
        GlobalScope.launch {
            favoriteDatabase?.tvShowDao()?.deleteTvShow(id)
            isFavorite.postValue(false)
        }
    }

    internal fun getMovieDetail(): MutableLiveData<Movie> {
        if (movieDetail.value == null) {
            setMovieDetail()
        }
        return movieDetail
    }

    internal fun getTvShowDetail(): MutableLiveData<TvShow> {
        if (tvShowDetail.value == null) {
            setTvShowDetail()
        }
        return tvShowDetail
    }
}