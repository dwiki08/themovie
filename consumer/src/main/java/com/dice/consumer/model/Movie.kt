package com.dice.consumer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: String,
    var title: String?,
    var poster_path: String?,
    var backdrop_path: String?,
    var release_date: String?,
    var overview: String?
): Parcelable