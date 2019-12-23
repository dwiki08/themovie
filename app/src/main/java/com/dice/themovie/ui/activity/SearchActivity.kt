package com.dice.themovie.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dice.themovie.R
import com.dice.themovie.adapter.MovieAdapter
import com.dice.themovie.adapter.TvShowAdapter
import com.dice.themovie.model.Movie
import com.dice.themovie.model.TvShow
import com.dice.themovie.utils.ViewModelFactory
import com.dice.themovie.viewmodel.FilmDataViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_movies.rvFilms
import kotlinx.android.synthetic.main.fragment_movies.swipeRefresh

class SearchActivity : AppCompatActivity() {

    private lateinit var filmDataViewModel: FilmDataViewModel
    private var type: String? = null
    private var query: String? = null
    private var loadData: Unit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        filmDataViewModel = ViewModelProvider(
            this,
            ViewModelFactory().viewModelFactory { FilmDataViewModel(this) }).get(
            FilmDataViewModel::class.java
        )

        query = intent?.getStringExtra(MainActivity.EXTRA_QUERY)
        type = intent?.getStringExtra(MainActivity.EXTRA_TYPE)

        supportActionBar?.title = "$type : $query"

        if (type == MainActivity.TYPE_MOVIE) {
            loadData = filmDataViewModel.setMovies(query)
            setMovieList()
        } else {
            loadData = filmDataViewModel.setTvShow(query)
            setTvShowList()
        }

        setSwipeRefresh()
    }

    private fun setTvShowList() {
        val rvAdapter = TvShowAdapter(this)

        rvAdapter.setOnClickListener(object : TvShowAdapter.ClickListener {
            override fun onItemClick(data: TvShow, view: View) {
                val intent = Intent(this@SearchActivity, DetailTvShowActivity::class.java)
                intent.putExtra(DetailTvShowActivity.EXTRA_ID, data.id)

                startActivity(intent)
            }
        })

        rvFilms.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvAdapter
        }

        filmDataViewModel.getTvShow(false).observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                rvAdapter.setData(list)
                isNotFound(false)
            }else{
                isNotFound(true)
            }
        })
    }

    private fun setMovieList() {
        val rvAdapter = MovieAdapter(this)

        rvAdapter.setOnItemClickListener(object : MovieAdapter.ClickListener {
            override fun onItemClick(data: Movie, v: View) {
                val intent = Intent(this@SearchActivity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_ID_STRING, data.id)

                startActivity(intent)
            }
        })

        rvFilms.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvAdapter
        }

        filmDataViewModel.getMovies(false).observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                rvAdapter.setData(list)
                isNotFound(false)
            } else {
                isNotFound(true)
            }
        })
    }

    private fun isNotFound(boolean: Boolean){
        if (boolean){
            rvFilms.visibility = View.GONE
            lottieNotFound.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
        }else{
            rvFilms.visibility = View.VISIBLE
            lottieNotFound.visibility = View.GONE
            tvNotFound.visibility = View.GONE
        }
    }

    private fun setSwipeRefresh() {
        filmDataViewModel.isLoading.observe(this, Observer { loading ->
            swipeRefresh.isRefreshing = loading
        })

        swipeRefresh.setOnRefreshListener {
            loadData
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
