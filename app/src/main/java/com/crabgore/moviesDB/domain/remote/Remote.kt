package com.crabgore.moviesDB.domain.remote

import com.crabgore.moviesDB.data.*
import io.reactivex.Single

interface Remote {
    fun getNowPlayingMovies(page: Int?): Single<MoviesResponse>
    fun getPopularMovies(page: Int?): Single<MoviesResponse>
    fun getTopRatedMovies(page: Int?): Single<MoviesResponse>
    fun getUpcomingMovies(page: Int?): Single<MoviesResponse>
    fun getOnTheAirTVs(page: Int?): Single<TVResponse>
    fun getPopularTVs(page: Int?): Single<TVResponse>
    fun getTopRatedTVs(page: Int?): Single<TVResponse>
    fun getPopularPeople(page: Int?): Single<PeopleResponse>
    fun getMovieDetails(movieId: Int): Single<MovieDetailsResponse>
    fun getTvDetails(tvId: Int): Single<TVDetailsResponse>
    fun getPeopleDetails(personId: Int): Single<PeopleDetailsResponse>
    fun getSearchMovieResults(query: String): Single<SearchMovieResponse>
    fun getSearchTVResults(query: String): Single<SearchTVResponse>
    fun getSearchPeopleResults(query: String): Single<SearchPeopleResponse>

    fun getToken(): Single<TokenResponse>
    fun authWithLogin(request: AuthWithLoginRequest): Single<TokenResponse>
    fun sessionId(request: RequestToken): Single<SessionResponse>
    fun getAccountDetails(session: String): Single<AccountResponse>
    fun logOut(session: LogoutRequest): Single<DeleteSessionResponse>

    fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int?): Single<MoviesResponse>
    fun getFavoriteTVs(accountId: Int, sessionId: String, page: Int?): Single<TVResponse>
    fun markAsFavorite(accountId: Int, sessionId: String, request: MarkAsFavoriteRequest): Single<MarkAsFavoriteResponse>
    fun getMovieAccountState(movieId: Int, sessionId: String): Single<AccountStateResponse>
    fun getTVAccountState(tvId: Int, sessionId: String): Single<AccountStateResponse>
}