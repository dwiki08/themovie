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
import com.dice.themovie.adapter.FavoriteTvShowAdapter
import com.dice.themovie.model.TvShow
import com.dice.themovie.ui.activity.DetailTvShowActivity
import com.dice.themovie.utils.ViewModelFactory
import com.dice.themovie.viewmodel.FilmDataViewModel
import kotlinx.android.synthetic.main.fragment_favorite_tv_show.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTvShowFragment : Fragment() {

    private lateinit var filmDataViewModel: FilmDataViewModel
    private lateinit var rvAdapter: FavoriteTvShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmDataViewModel = ViewModelProvider(this, ViewModelFactory().viewModelFactory { FilmDataViewModel(context!!)}).get(FilmDataViewModel::class.java)

        rvAdapter = FavoriteTvShowAdapter(context)
    }

    override fun onResume() {
        super.onResume()

        setList()
    }

    private fun setList(){
        rvAdapter.setOnClickListener(object : FavoriteTvShowAdapter.ClickListener{
            override fun onItemClick(data: TvShow, view: View) {
                val intent = Intent(context, DetailTvShowActivity::class.java)
                intent.putExtra(DetailTvShowActivity.EXTRA_ID, data.id)

                startActivity(intent)
            }
        })

        rvFavorite.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = rvAdapter
        }

        filmDataViewModel.getFavoriteTvShowList().observe(this, Observer {list ->
            if (list != null){
                if (list.isNotEmpty()) tvEmpty.visibility = View.GONE
                rvAdapter.setData(list)
            }
        })
    }

}
