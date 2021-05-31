package com.crabgore.moviesDB.data

import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @GET("movie/now_playing")
    fun nowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Observable<MoviesResponse>

    @GET("movie/popular")
    fun popularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<MoviesResponse>

    @GET("movie/top_rated")
    fun topRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<MoviesResponse>

    @GET("movie/upcoming")
    fun upcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Observable<MoviesResponse>

    @GET("tv/on_the_air")
    fun onTheAirTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<TVResponse>

    @GET("tv/popular")
    fun popularTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<TVResponse>

    @GET("tv/top_rated")
    fun topRatedTVs(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<TVResponse>

    @GET("person/popular")
    fun popularPeople(
        @Query("api_key") apiKey: String,
        @Query("language") language: String?,
        @Query("page") page: Int?
    ): Observable<PeopleResponse>

    @GET("movie/{movie_id}")
    fun movieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<MovieDetailsResponse>

    @GET("tv/{tv_id}")
    fun tvDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<TVDetailsResponse>

    @GET("person/{person_id}")
    fun peopleDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<PeopleDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    fun peopleMovieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<PeopleCreditsResponse>

    @GET("person/{person_id}/tv_credits")
    fun peopleTVCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<TVCastResponse>

    @GET("movie/{movie_id}/credits")
    fun movieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<MovieCreditsResponse>

    @GET("tv/{tv_id}/credits")
    fun tvCredits(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<MovieCreditsResponse>

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<SearchMovieResponse>

    @GET("search/tv")
    fun searchTV(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<SearchTVResponse>

    @GET("search/person")
    fun searchPeople(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String,
        @Query("language") language: String?
    ): Observable<SearchPeopleResponse>

    @GET("authentication/token/new")
    fun requestToken(
        @Query("api_key") apiKey: String
    ): Observable<TokenResponse>

    @POST("authentication/token/validate_with_login")
    fun authWithLogin(
        @Query("api_key") apiKey: String,
        @Body request: AuthWithLoginRequest
    ): Observable<TokenResponse>

    @POST("authentication/session/new")
    fun sessionId(
        @Query("api_key") apiKey: String,
        @Body request: RequestToken
    ): Observable<SessionResponse>

    @GET("account")
    fun accountDetails(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Observable<AccountResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    fun logOut(
        @Query("api_key") apiKey: String,
        @Body request: LogoutRequest
    ): Observable<DeleteSessionResponse>
}