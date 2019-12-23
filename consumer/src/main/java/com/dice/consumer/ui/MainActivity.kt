package com.dice.consumer.ui

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.dice.consumer.DatabaseContract
import com.dice.consumer.R
import com.dice.consumer.adapter.MovieAdapter
import com.dice.consumer.model.Movie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvAdapter = MovieAdapter(this@MainActivity)

        if (getMovieList() != null) {
            rvAdapter.setData(getMovieList()!!)
        }

        rvList.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = rvAdapter
        }
    }

    private fun getMovieList(): ArrayList<Movie>? {
        val cursor = contentResolver?.query(
            DatabaseContract.FavoriteMovieColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val list = cursor?.let { cursorToListMovie(it) }

        Log.d("myProvider", "consumer list : ${list?.size}")

        cursor?.close()

        return list
    }

    private fun cursorToListMovie(cursor: Cursor): ArrayList<Movie> {
        val list = ArrayList<Movie>()

        while (cursor.moveToNext()){
            DatabaseContract.FavoriteMovieColumns.let {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.TITLE))
                val posterPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.POSTER_PATH))
                val backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.BACKDROP_PATH))
                val release = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.RELEASE_DATE))
                val overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.OVERVIEW))

                list.add(Movie(id.toString(), title, posterPath, backdropPath, release, overview))
            }
        }

        return list
    }
}
