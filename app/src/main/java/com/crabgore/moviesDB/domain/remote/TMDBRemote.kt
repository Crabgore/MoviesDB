package com.crabgore.moviesDB.domain.remote

import android.content.Context
import android.os.Build
import com.crabgore.moviesDB.Const.Addresses.Companion.API_HOST
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.data.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Inject

class TMDBRemote @Inject constructor(
    context: Context
) : Remote {

    private val language = Locale.getDefault().toLanguageTag()
    private val region = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0].country
    } else {
        context.resources.configuration.locale.country
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(API_HOST)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl(API_HOST)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)
    private val service2 = retrofit2.create(ApiService::class.java)

    override suspend fun getNowPlayingMovies(page: Int?): Response<MoviesResponse> {
        return service2.nowPlayingMovies(API_KEY, language, page, region)
    }

    override suspend fun getPopularMovies(page: Int?): Response<MoviesResponse> {
        return service.popularMovies(API_KEY, language, page)
    }

    override suspend fun getTopRatedMovies(page: Int?): Response<MoviesResponse> {
        return service.topRatedMovies(API_KEY, language, page)
    }

    override suspend fun getUpcomingMovies(page: Int?): Response<MoviesResponse> {
        return service.upcomingMovies(API_KEY, language, page, region)
    }

    override suspend fun getOnTheAirTVs(page: Int?): Response<TVResponse> {
        return service.onTheAirTVs(API_KEY, language, page)
    }

    override suspend fun getPopularTVs(page: Int?): Response<TVResponse> {
        return service.popularTVs(API_KEY, language, page)
    }

    override suspend fun getTopRatedTVs(page: Int?): Response<TVResponse> {
        return service.topRatedTVs(API_KEY, language, page)
    }

    override suspend fun getPopularPeople(page: Int?): Response<PeopleResponse> {
        return service.popularPeople(API_KEY, language, page)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<MovieDetailsResponse> {
        return service.movieDetails(movieId, API_KEY, language, "credits")
    }

    override suspend fun getTvDetails(tvId: Int): Response<TVDetailsResponse> {
        return service.tvDetails(tvId, API_KEY, language, "credits")
    }

    override suspend fun getPeopleDetails(personId: Int): Response<PeopleDetailsResponse> {
        return service.peopleDetails(personId, API_KEY, language, "movie_credits,tv_credits")
    }

    override suspend fun getSearchMovieResults(query: String): Response<SearchMovieResponse> {
        return service.searchMovie(query, API_KEY, language)
    }

    override suspend fun getSearchTVResults(query: String): Response<SearchTVResponse> {
        return service.searchTV(query, API_KEY, language)
    }

    override suspend fun getSearchPeopleResults(query: String): Response<SearchPeopleResponse> {
        return service.searchPeople(query, API_KEY, language)
    }



    override suspend fun getToken(): Response<TokenResponse> {
        return service.requestToken(API_KEY)
    }

    override suspend fun authWithLogin(request: AuthWithLoginRequest): Response<TokenResponse> {
        return service.authWithLogin(API_KEY, request)
    }

    override suspend fun sessionId(request: RequestToken): Response<SessionResponse> {
        return service.sessionId(API_KEY, request)
    }

    override suspend fun getAccountDetails(session: String): Response<AccountResponse> {
        return service.accountDetails(API_KEY, session)
    }

    override suspend fun logOut(session: LogoutRequest): Response<DeleteSessionResponse> {
        return service.logOut(API_KEY, session)
    }



    override suspend fun getFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int?
    ): Response<MoviesResponse> {
        return service.favoriteMovies(accountId, sessionId, API_KEY, language, page)
    }

    override suspend fun getFavoriteTVs(accountId: Int, sessionId: String, page: Int?): Response<TVResponse> {
        return service.favoriteTVs(accountId, sessionId, API_KEY, language, page)
    }

    override suspend fun markAsFavorite(
        accountId: Int,
        sessionId: String,
        request: MarkAsFavoriteRequest
    ): Response<MarkAsFavoriteResponse> {
        return service.markAsFavorite(accountId, sessionId, API_KEY, request)
    }

    override suspend fun getMovieAccountState(
        movieId: Int,
        sessionId: String
    ): Response<AccountStateResponse> {
        return service.movieAccountState(movieId, API_KEY, sessionId)
    }

    override suspend fun getTVAccountState(tvId: Int, sessionId: String): Response<AccountStateResponse> {
        return service.tvAccountState(tvId, API_KEY, sessionId)
    }
}