package com.dice.themovie.model.local

import android.database.Cursor
import androidx.room.*
import com.dice.themovie.model.Movie
import com.dice.themovie.model.TvShow

interface DatabaseDAO{

    @Dao
    interface FavoriteMovie {
        @Query("SELECT * FROM favorite_movie")
        fun getMovies(): List<Movie>

        @Query("SELECT * FROM favorite_movie WHERE id = :id")
        fun getMovie(id: String): Movie

        @Query("SELECT * FROM favorite_movie")
        fun getMoviesCursor(): Cursor

        @Query("SELECT * FROM favorite_movie WHERE id = :id")
        fun getMoviesCursor(id: String): Cursor

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertMovie(movie: Movie)

        @Query("DELETE FROM favorite_movie WHERE id = :id")
        fun deleteMovie(id: String)
    }

    @Dao
    interface FavoriteTvShow {
        @Query("SELECT * FROM favorite_tv_show")
        fun getTvShow(): List<TvShow>

        @Query("SELECT * FROM favorite_tv_show WHERE id = :id")
        fun getTvShow(id: String): TvShow

        @Query("SELECT * FROM favorite_tv_show")
        fun getTvShowCursor(): Cursor

        @Query("SELECT * FROM favorite_tv_show WHERE id = :id")
        fun getTvShowCursor(id: String): Cursor

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertTvShow(tvShow: TvShow)

        @Query("DELETE FROM favorite_tv_show WHERE id = :id")
        fun deleteTvShow(id: String)
    }

}

