package com.crabgore.moviesDB.data.favorites.requests

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class MarkAsFavoriteRequest(
    var media_type: String,
    var media_id: Int,
    var favorite: Boolean
) : Parcelable