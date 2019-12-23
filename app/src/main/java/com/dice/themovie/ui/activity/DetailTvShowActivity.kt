package com.dice.themovie.ui.activity

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
import com.dice.themovie.model.TvShow
import com.dice.themovie.utils.ViewModelFactory
import com.dice.themovie.viewmodel.FilmDetailViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_tv_show.*

class DetailTvShowActivity : AppCompatActivity(){

    private lateinit var filmDetailViewModel: FilmDetailViewModel
    private lateinit var filmModel: TvShow
    private var filmId = ""
    private var isFavorite = false

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getStringExtra(EXTRA_ID) != null){
            filmId = intent.getStringExtra(EXTRA_ID)!!
        }

        filmDetailViewModel = ViewModelProvider(
            this,
            ViewModelFactory().viewModelFactory { FilmDetailViewModel(this, filmId) }).get(
            FilmDetailViewModel::class.java
        )

        setLoading()

        filmDetailViewModel.getTvShowDetail().observe(this, Observer {filmModel ->
            this.filmModel = filmModel

            setAppBar(filmModel.original_name)

            tvTitle.text = filmModel.original_name

            Glide.with(this)
                .load(ApiClient.POSTER_URL + filmModel.poster_path)
                .placeholder(R.color.placeholder)
                .error(R.drawable.empty)
                .into(imgPoster)

            Glide.with(this)
                .load(ApiClient.BACKDROP_URL + filmModel.backdrop_path)
                .placeholder(R.color.placeholder)
                .error(R.drawable.empty)
                .into(imgBackdrop)

            tvDateRelease.text = filmModel.first_air_date
            tvOverview.text = filmModel.overview
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
        filmDetailViewModel.checkIsFavoriteTvShow(id).observe(this, Observer { isFavorite ->
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

                if (!isFavorite) {
                    filmDetailViewModel.setFavoriteTvShow(filmModel)
                    showMessage(getString(R.string.added_to_favorite))
                } else {
                    filmDetailViewModel.deleteFavoriteTvShow(filmModel.id)
                    showMessage(getString(R.string.removed_from_favorite))
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
