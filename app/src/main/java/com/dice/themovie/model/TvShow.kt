package com.dice.themovie.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dice.themovie.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = Constants.TABLE_TV_SHOW)
data class TvShow(
    @PrimaryKey
    @ColumnInfo
    var id: String,

    @ColumnInfo
    var original_name: String?,

    @ColumnInfo
    var poster_path: String?,

    @ColumnInfo
    var backdrop_path: String?,

    @ColumnInfo
    var first_air_date: String?,

    @ColumnInfo
    var overview: String?
): Parcelable