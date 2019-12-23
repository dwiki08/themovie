package com.dice.themovie.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dice.themovie.R
import com.dice.themovie.model.network.ApiClient
import com.dice.themovie.model.Movie
import com.dice.themovie.ui.widget.StacksWidget
import com.dice.themovie.utils.ViewModelFactory
import com.dice.themovie.viewmodel.FilmDetailViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovieActivity : AppCompatActivity(){
    private lateinit var filmDetailViewModel: FilmDetailViewModel
    private var filmId = ""
    private lateinit var filmModel: Movie
    private var isFavorite = false

    companion object {
        const val EXTRA_ID_STRING = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getStringExtra(EXTRA_ID_STRING) != null){
            filmId = intent.getStringExtra(EXTRA_ID_STRING)!!
        }

        filmDetailViewModel = ViewModelProvider(
            this,
            ViewModelFactory().viewModelFactory { FilmDetailViewModel(this, filmId) }).get(
            FilmDetailViewModel::class.java
        )

        setLoading()

        filmDetailViewModel.getMovieDetail().observe(this, Observer {movie ->
            if (movie != null){
                filmModel = movie

                setAppBar(movie.title)

                tvTitle.text = movie.title

                Glide.with(this)
                    .load(ApiClient.POSTER_URL + movie.poster_path)
                    .placeholder(R.color.placeholder)
                    .error(R.drawable.empty)
                    .into(imgPoster)

                Glide.with(this)
                    .load(ApiClient.BACKDROP_URL + movie.backdrop_path)
                    .placeholder(R.color.placeholder)
                    .error(R.drawable.empty)
                    .into(imgBackdrop)

                tvDateRelease.text = movie.release_date
                tvOverview.text = movie.overview
            }
        })
    }

    private fun setLoading(){
        filmDetailViewModel.isLoading.observe(this, Observer {loading ->
            frameLoading.visibility = if (loading){
                View.VISIBLE
            }else{
                View.GONE
            }
        })

        filmDetailViewModel.isError.observe(this, Observer { error ->
            frameError.visibility = if (error){
                View.VISIBLE
            }else{
                View.GONE
            }
        })
    }

    private fun setAppBar(title: String?) {
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (offset < -500) {
                collapsingToolbar?.title = title
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
            } else {
                collapsingToolbar?.title = ""
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            }
        })
    }

    private fun checkFavorite(menu: Menu, id: String) {
        filmDetailViewModel.checkIsFavoriteMovie(id).observe(this, Observer { isFavorite ->
            this.isFavorite = isFavorite

            appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
                if (offset < -500) {
                    if (isFavorite){
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_accent_24dp)
                    }else {
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp)
                    }
                } else {
                    if (isFavorite){
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_accent_24dp)
                    }else {
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_border_white_24dp)
                    }
                }
            })
        })
    }

    private fun updateWidget() {
        val intent = Intent(this, StacksWidget::class.java)
        intent.action = StacksWidget.UPDATE_WIDGET
        this.sendBroadcast(intent)
    }

    private fun showMessage(message: String) {
        Snackbar.make(viewParent, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_detail_toolbar, menu)
        checkFavorite(menu, filmId)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_favorite -> {
                updateWidget()

                if (!isFavorite) {
                    filmDetailViewModel.setFavoriteMovie(filmModel)
                    showMessage(getString(R.string.added_to_favorite))
                } else {
                    filmDetailViewModel.deleteFavoriteMovie(filmModel.id)
                    showMessage(getString(R.string.removed_from_favorite))
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
