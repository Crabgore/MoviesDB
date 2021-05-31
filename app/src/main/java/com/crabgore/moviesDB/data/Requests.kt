package com.crabgore.moviesDB.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class AuthWithLoginRequest(
    var username: String,
    var password: String,
    var request_token: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class RequestToken(
    var request_token: String
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class LogoutRequest(
    var session_id: String
) : Parcelable