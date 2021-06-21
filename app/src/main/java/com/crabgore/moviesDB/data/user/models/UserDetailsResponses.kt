package com.crabgore.moviesDB.data.user.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountResponse (
    val avatar: Avatar? = null,
    val id: Int,
    val iso639_1: String? = null,
    val iso3166_1: String? = null,
    val name: String? = null,
    val includeAdult: Boolean? = null,
    val username: String? = null
)

@JsonClass(generateAdapter = true)
data class Avatar (
    val gravatar: Gravatar? = null,
    val tmdb: Tmdb? = null
)

@JsonClass(generateAdapter = true)
data class Gravatar (
    val hash: String? = null
)

@JsonClass(generateAdapter = true)
data class Tmdb (
    val avatarPath: String? = null
)