package com.dice.themovie.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dice.themovie.R
import com.dice.themovie.adapter.TvShowAdapter
import com.dice.themovie.model.TvShow
import com.dice.themovie.ui.activity.DetailTvShowActivity
import com.dice.themovie.utils.ViewModelFactory
import com.dice.themovie.viewmodel.FilmDataViewModel
import kotlinx.android.synthetic.main.fragment_tv_shows.*

/**
 * A simple [Fragment] subclass.
 */
class TvShowsFragment : Fragment() {

    private lateinit var filmDataViewModel: FilmDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmDataViewModel = ViewModelProvider(
            this,
            ViewModelFactory().viewModelFactory { FilmDataViewModel(context!!) }).get(
            FilmDataViewModel::class.java
        )

        setList()
        setError()
        setSwipeRefresh()
    }

    private fun setList() {
        val rvAdapter = TvShowAdapter(context)

        rvAdapter.setOnClickListener(object : TvShowAdapter.ClickListener {
            override fun onItemClick(data: TvShow, view: View) {
                val intent = Intent(context, DetailTvShowActivity::class.java)
                intent.putExtra(DetailTvShowActivity.EXTRA_ID, data.id)

                startActivity(intent)
            }

        })

        rvFilms.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvAdapter
        }

        filmDataViewModel.getTvShow(true).observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                rvAdapter.setData(list)
            }
        })
    }

    private fun setSwipeRefresh() {
        filmDataViewModel.isLoading.observe(this, Observer { loading ->
            swipeRefresh.isRefreshing = loading
        })

        swipeRefresh.setOnRefreshListener {
            filmDataViewModel.setTvShow(null)
        }
    }

    private fun setError(){
        filmDataViewModel.isError.observe(this, Observer {error ->
            if (error){
                rvFilms.visibility = View.GONE
                lottieError.visibility = View.VISIBLE
                tvError.visibility = View.VISIBLE
            }else{
                rvFilms.visibility = View.VISIBLE
                lottieError.visibility = View.GONE
                tvError.visibility = View.GONE
            }
        })
    }
}
