package com.crabgore.moviesDB.data.favorites.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkAsFavoriteResponse (
    val success: Boolean? = null,
    @field:Json(name = "status_code")
    val statusCode: Int? = null,
    @field:Json(name = "status_message")
    val statusMessage: String? = null
)