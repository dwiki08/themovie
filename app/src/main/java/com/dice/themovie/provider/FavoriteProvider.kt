package com.dice.themovie.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.dice.themovie.model.local.FavoriteDatabase
import com.dice.themovie.utils.Constants

class FavoriteProvider : ContentProvider() {

    companion object {
        private var favoriteDatabase: FavoriteDatabase? = null

        private const val MOVIE = 10
        private const val MOVIE_ID = 11

        private const val TV_SHOW = 20
        private const val TV_SHOW_ID = 21

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(Constants.AUTHORITY, Constants.TABLE_MOVIE, MOVIE)
            uriMatcher.addURI(Constants.AUTHORITY, "${Constants.TABLE_MOVIE}/#", MOVIE_ID)

            uriMatcher.addURI(Constants.AUTHORITY, Constants.TABLE_TV_SHOW, TV_SHOW)
            uriMatcher.addURI(Constants.AUTHORITY, "${Constants.TABLE_TV_SHOW}/#", TV_SHOW_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteDatabase = FavoriteDatabase.getInstance(context!!)

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            MOVIE -> {
                Log.d("myProvider", "getting query")
                favoriteDatabase?.movieDao()?.getMoviesCursor()
            }

            MOVIE_ID -> favoriteDatabase?.movieDao()?.getMoviesCursor(
                ContentUris.parseId(uri).toString()
            )

            TV_SHOW -> favoriteDatabase?.tvShowDao()?.getTvShowCursor()

            TV_SHOW_ID -> favoriteDatabase?.tvShowDao()?.getTvShowCursor(
                ContentUris.parseId(uri).toString()
            )

            else -> {
                Log.d("myProvider", "failed getting query")
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
