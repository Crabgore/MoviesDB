package com.crabgore.moviesDB.data.user.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse (
    val success: Boolean? = null,
    val statusCode: Int? = null,
    val statusMessage: String? = null
)

@JsonClass(generateAdapter = true)
data class TokenResponse (
    val success: Boolean? = null,
    val expiresAt: String? = null,
    @field:Json(name = "request_token")
    val requestToken: String? = null
)

@JsonClass(generateAdapter = true)
data class SessionResponse (
    val success: Boolean? = null,
    @field:Json(name = "session_id")
    val sessionID: String? = null
)

@JsonClass(generateAdapter = true)
data class DeleteSessionResponse (
    val success: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class Rated (
    val value: Long? = null
)