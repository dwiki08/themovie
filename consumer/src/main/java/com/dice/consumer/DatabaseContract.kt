package com.dice.consumer

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavoriteMovieColumns : BaseColumns {
        companion object {
            private const val AUTHORITY = "com.dice.themovie"
            private const val SCHEME = "content"

            private const val TABLE_NAME = "favorite_movie"
            const val ID = "id"
            const val TITLE = "title"
            const val POSTER_PATH = "poster_path"
            const val BACKDROP_PATH = "backdrop_path"
            const val RELEASE_DATE = "release_date"
            const val OVERVIEW = "overview"

            // create URI content://com.die.themovie/favorite_movie
            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}