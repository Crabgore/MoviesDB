package com.crabgore.moviesDB.domain.remote

import android.content.Context
import android.os.Build
import com.crabgore.moviesDB.Const.Addresses.Companion.API_HOST
import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.data.*
import io.reactivex.Single
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

    private val service = retrofit.create(ApiService::class.java)

    override fun getNowPlayingMovies(page: Int?): Single<MoviesResponse> {
        return service.nowPlayingMovies(API_KEY, language, page, region).singleOrError()
    }

    override fun getPopularMovies(page: Int?): Single<MoviesResponse> {
        return service.popularMovies(API_KEY, language, page).singleOrError()
    }

    override fun getTopRatedMovies(page: Int?): Single<MoviesResponse> {
        return service.topRatedMovies(API_KEY, language, page).singleOrError()
    }

    override fun getUpcomingMovies(page: Int?): Single<MoviesResponse> {
        return service.upcomingMovies(API_KEY, language, page, region).singleOrError()
    }

    override fun getOnTheAirTVs(page: Int?): Single<TVResponse> {
        return service.onTheAirTVs(API_KEY, language, page).singleOrError()
    }

    override fun getPopularTVs(page: Int?): Single<TVResponse> {
        return service.popularTVs(API_KEY, language, page).singleOrError()
    }

    override fun getTopRatedTVs(page: Int?): Single<TVResponse> {
        return service.topRatedTVs(API_KEY, language, page).singleOrError()
    }

    override fun getPopularPeople(page: Int?): Single<PeopleResponse> {
        return service.popularPeople(API_KEY, language, page).singleOrError()
    }

    override fun getMovieDetails(movieId: Int): Single<MovieDetailsResponse> {
        return service.movieDetails(movieId, API_KEY, language, "credits").singleOrError()
    }

    override fun getTvDetails(tvId: Int): Single<TVDetailsResponse> {
        return service.tvDetails(tvId, API_KEY, language, "credits").singleOrError()
    }

    override fun getPeopleDetails(personId: Int): Single<PeopleDetailsResponse> {
        return service.peopleDetails(personId, API_KEY, language, "movie_credits,tv_credits").singleOrError()
    }

    override fun getSearchMovieResults(query: String): Single<SearchMovieResponse> {
        return service.searchMovie(query, API_KEY, language).singleOrError()
    }

    override fun getSearchTVResults(query: String): Single<SearchTVResponse> {
        return service.searchTV(query, API_KEY, language).singleOrError()
    }

    override fun getSearchPeopleResults(query: String): Single<SearchPeopleResponse> {
        return service.searchPeople(query, API_KEY, language).singleOrError()
    }



    override fun getToken(): Single<TokenResponse> {
        return service.requestToken(API_KEY).singleOrError()
    }

    override fun authWithLogin(request: AuthWithLoginRequest): Single<TokenResponse> {
        return service.authWithLogin(API_KEY, request).singleOrError()
    }

    override fun sessionId(request: RequestToken): Single<SessionResponse> {
        return service.sessionId(API_KEY, request).singleOrError()
    }

    override fun getAccountDetails(session: String): Single<AccountResponse> {
        return service.accountDetails(API_KEY, session).singleOrError()
    }

    override fun logOut(session: LogoutRequest): Single<DeleteSessionResponse> {
        return service.logOut(API_KEY, session).singleOrError()
    }



    override fun getFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int?
    ): Single<MoviesResponse> {
        return service.favoriteMovies(accountId, sessionId, API_KEY, language, page).singleOrError()
    }

    override fun getFavoriteTVs(accountId: Int, sessionId: String, page: Int?): Single<TVResponse> {
        return service.favoriteTVs(accountId, sessionId, API_KEY, language, page).singleOrError()
    }

    override fun markAsFavorite(
        accountId: Int,
        sessionId: String,
        request: MarkAsFavoriteRequest
    ): Single<MarkAsFavoriteResponse> {
        return service.markAsFavorite(accountId, sessionId, API_KEY, request).singleOrError()
    }

    override fun getMovieAccountState(
        movieId: Int,
        sessionId: String
    ): Single<AccountStateResponse> {
        return service.movieAccountState(movieId, API_KEY, sessionId).singleOrError()
    }

    override fun getTVAccountState(tvId: Int, sessionId: String): Single<AccountStateResponse> {
        return service.tvAccountState(tvId, API_KEY, sessionId).singleOrError()
    }
}