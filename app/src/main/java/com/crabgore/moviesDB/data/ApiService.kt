package com.crabgore.moviesDB.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("movie/now_playing")
    suspend fun nowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Response<MoviesResponse>

    @GET("movie/popular")
    suspend fun popularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun topRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun upcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Response<MoviesResponse>

    @GET("tv/on_the_air")
    suspend fun onTheAirTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>

    @GET("tv/popular")
    suspend fun popularTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>

    @GET("tv/top_rated")
    suspend fun topRatedTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>

    @GET("person/popular")
    suspend fun popularPeople(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<PeopleResponse>

    @GET("movie/{movie_id}")
    suspend fun movieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<MovieDetailsResponse>

    @GET("tv/{tv_id}")
    suspend fun tvDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<TVDetailsResponse>

    @GET("person/{person_id}")
    suspend fun peopleDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): Response<PeopleDetailsResponse>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchMovieResponse>

    @GET("search/tv")
    suspend fun searchTV(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchTVResponse>

    @GET("search/person")
    suspend fun searchPeople(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Response<SearchPeopleResponse>

    @GET("authentication/token/new")
    suspend fun requestToken(
        @Query("api_key") apiKey: String
    ): Response<TokenResponse>

    @POST("authentication/token/validate_with_login")
    suspend fun authWithLogin(
        @Query("api_key") apiKey: String,
        @Body request: AuthWithLoginRequest
    ): Response<TokenResponse>

    @POST("authentication/session/new")
    suspend fun sessionId(
        @Query("api_key") apiKey: String,
        @Body request: RequestToken
    ): Response<SessionResponse>

    @GET("account")
    suspend fun accountDetails(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun logOut(
        @Query("api_key") apiKey: String,
        @Body request: LogoutRequest
    ): Response<DeleteSessionResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun favoriteMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<MoviesResponse>

    @GET("account/{account_id}/favorite/tv")
    suspend fun favoriteTVs(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Response<TVResponse>


    @POST("account/{account_id}/favorite")
    suspend fun markAsFavorite(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body request: MarkAsFavoriteRequest
    ): Response<MarkAsFavoriteResponse>

    @GET("movie/{movie_id}/account_states")
    suspend fun movieAccountState(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountStateResponse>

    @GET("tv/{tv_id}/account_states")
    suspend fun tvAccountState(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<AccountStateResponse>
}