package com.crabgore.moviesDB.domain.remote

import com.crabgore.moviesDB.data.*
import retrofit2.Response

interface Remote {
    suspend fun getNowPlayingMovies(page: Int?): Response<MoviesResponse>
    suspend fun getPopularMovies(page: Int?): Response<MoviesResponse>
    suspend fun getTopRatedMovies(page: Int?): Response<MoviesResponse>
    suspend fun getUpcomingMovies(page: Int?): Response<MoviesResponse>
    suspend fun getOnTheAirTVs(page: Int?): Response<TVResponse>
    suspend fun getPopularTVs(page: Int?): Response<TVResponse>
    suspend fun getTopRatedTVs(page: Int?): Response<TVResponse>
    suspend fun getPopularPeople(page: Int?): Response<PeopleResponse>
    suspend fun getMovieDetails(movieId: Int): Response<MovieDetailsResponse>
    suspend fun getTvDetails(tvId: Int): Response<TVDetailsResponse>
    suspend fun getPeopleDetails(personId: Int): Response<PeopleDetailsResponse>
    suspend fun getSearchMovieResults(query: String): Response<SearchMovieResponse>
    suspend fun getSearchTVResults(query: String): Response<SearchTVResponse>
    suspend fun getSearchPeopleResults(query: String): Response<SearchPeopleResponse>

    suspend fun getToken(): Response<TokenResponse>
    suspend fun authWithLogin(request: AuthWithLoginRequest): Response<TokenResponse>
    suspend fun sessionId(request: RequestToken): Response<SessionResponse>
    suspend fun getAccountDetails(session: String): Response<AccountResponse>
    suspend fun logOut(session: LogoutRequest): Response<DeleteSessionResponse>

    suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int?): Response<MoviesResponse>
    suspend fun getFavoriteTVs(accountId: Int, sessionId: String, page: Int?): Response<TVResponse>
    suspend fun markAsFavorite(accountId: Int, sessionId: String, request: MarkAsFavoriteRequest): Response<MarkAsFavoriteResponse>
    suspend fun getMovieAccountState(movieId: Int, sessionId: String): Response<AccountStateResponse>
    suspend fun getTVAccountState(tvId: Int, sessionId: String): Response<AccountStateResponse>
}