package com.dice.themovie.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.dice.themovie.R
import com.dice.themovie.model.Movie
import com.dice.themovie.model.local.FavoriteDatabase
import com.dice.themovie.model.network.ApiClient
import com.dice.themovie.ui.widget.StacksWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StackRemoteViewsFactory(val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val favoriteDatabase = FavoriteDatabase.getInstance(context)
    private var movieList = mutableListOf<Movie>()


    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun onDataSetChanged() {
        Log.d("stackWidget", "data onchange")

        GlobalScope.launch {
            val list = favoriteDatabase?.movieDao()?.getMovies()

            if (list != null){
                movieList.clear()
                movieList.addAll(list)
            }
        }

    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(
            context.packageName,
            R.layout.item_widget
        )

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(ApiClient.POSTER_URL + movieList[position].poster_path)
            .submit()
            .get()

        rv.setImageViewBitmap(R.id.imgView, bitmap)

        val intent = Intent().putExtra(StacksWidget.EXTRA_ITEM, movieList[position].id)

        rv.setOnClickFillInIntent(R.id.imgView, intent)
        return rv
    }

    override fun getCount(): Int {
        return movieList.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {

    }

}