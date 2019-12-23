package com.dice.themovie.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dice.themovie.R
import com.dice.themovie.model.Movie
import com.dice.themovie.model.MovieList
import com.dice.themovie.model.TvShow
import com.dice.themovie.model.TvShowList
import com.dice.themovie.model.local.FavoriteDatabase
import com.dice.themovie.model.network.ApiClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmDataViewModel(context: Context) : ViewModel() {
    companion object {
        private val TAG = FilmDataViewModel::class.java.simpleName
    }

    private val apiService = ApiClient.createService()
    private val apiKey = ApiClient.API_KEY
    //private val language = context.getString(R.string.language) //not all items have translated language
    private val language = context.getString(R.string.default_language)

    private val favoriteDatabase = FavoriteDatabase.getInstance(context)

    private val movieList: MutableLiveData<List<Movie>> = MutableLiveData()
    private val tvShowList: MutableLiveData<List<TvShow>> = MutableLiveData()
    private val favoriteMovieList: MutableLiveData<List<Movie>> = MutableLiveData()
    private val favoriteTvShowList: MutableLiveData<List<TvShow>> = MutableLiveData()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var isError: MutableLiveData<Boolean> = MutableLiveData()

    internal fun setMovies(queryTitle: String?) {
        isLoading.postValue(true)

        val getApiService: Call<MovieList> = if (queryTitle != null) {
            apiService.getMovie(apiKey, language, queryTitle)
        } else {
            apiService.getMovie(apiKey, language)
        }

        getApiService.enqueue(object : Callback<MovieList> {
            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val list = response.body() as MovieList

                    movieList.postValue(list.results)
                }
            }
        })
    }

    internal fun setTvShow(queryTitle: String?) {
        isLoading.postValue(true)

        val getApiService: Call<TvShowList> = if (queryTitle != null) {
            apiService.getTvShow(apiKey, language, queryTitle)
        } else {
            apiService.getTvShow(apiKey, language)
        }

        getApiService.enqueue(object : Callback<TvShowList> {
            override fun onFailure(call: Call<TvShowList>, t: Throwable) {
                isLoading.postValue(false)
                isError.postValue(true)
            }

            override fun onResponse(call: Call<TvShowList>, response: Response<TvShowList>) {
                if (response.isSuccessful) {
                    isLoading.postValue(false)
                    isError.postValue(false)

                    val list = response.body() as TvShowList

                    tvShowList.postValue(list.results)
                }
            }
        })
    }

    internal fun getMovies(init: Boolean): LiveData<List<Movie>> {
        if (movieList.value == null && init) {
            setMovies(null)
        }
        return movieList
    }

    internal fun getTvShow(init: Boolean): LiveData<List<TvShow>> {
        if (tvShowList.value == null && init) {
            setTvShow(null)
        }
        return tvShowList
    }

    internal fun getFavoriteMovieList(): MutableLiveData<List<Movie>> {
        GlobalScope.launch {
            val list = favoriteDatabase?.movieDao()?.getMovies()

            favoriteMovieList.postValue(list)
        }

        return favoriteMovieList
    }

    internal fun getFavoriteTvShowList(): MutableLiveData<List<TvShow>> {
        GlobalScope.launch {
            val list = favoriteDatabase?.tvShowDao()?.getTvShow()

            favoriteTvShowList.postValue(list)
        }

        return favoriteTvShowList
    }
}