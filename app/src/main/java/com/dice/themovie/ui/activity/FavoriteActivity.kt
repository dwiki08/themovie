package com.dice.themovie.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dice.themovie.R
import com.dice.themovie.adapter.ViewPagerAdapter
import com.dice.themovie.ui.fragment.FavoriteMoviesFragment
import com.dice.themovie.ui.fragment.FavoriteTvShowFragment
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite)

        setViewPager()
    }

    private fun setViewPager(){
        val fragmentList = listOf(
            FavoriteMoviesFragment(),
            FavoriteTvShowFragment()
        )

        val titleList = listOf(
            getString(R.string.movies),
            getString(R.string.tv_shows)
        )

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList, titleList)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> true
        }
    }
}
