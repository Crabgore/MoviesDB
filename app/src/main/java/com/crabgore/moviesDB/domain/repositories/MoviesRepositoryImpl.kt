package com.crabgore.moviesDB.domain.repositories

import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.AccountStateResponse
import com.crabgore.moviesDB.data.MovieDetailsResponse
import com.crabgore.moviesDB.data.MoviesResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.MoviesRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseRepository(), MoviesRepository {
    override var maxPages = Int.MAX_VALUE
    override var castListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    override var crewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    override suspend fun getNowPlayingMovies(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getNowPlayingMovies(page) }
        return parseResponse(response)
    }

    override suspend fun getPopularMovies(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getPopularMovies(page) }
        return parseResponse(response)
    }

    override suspend fun getTopRatedMovies(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getTopRatedMovies(page) }
        return parseResponse(response)
    }

    override suspend fun getUpcomingMovies(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getUpcomingMovies(page) }
        return parseResponse(response)
    }

    override suspend fun getFavoriteMovies(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall {
            remote.getFavoriteMovies(
                storage.getInt(ACCOUNT_ID),
                storage.getString(SESSION_ID)!!,
                page
            )
        }
        return parseResponse(response)
    }

    override suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse> {
        val response = apiCall { remote.getMovieDetails(id) }
        return parseMovieDetailsResponse(response)
    }

    override suspend fun getMovieAccountState(id: Int): Boolean {
        val response = apiCall { remote.getMovieAccountState(id, storage.getString(SESSION_ID)!!) }
        return parseAccountState(response)
    }

    private fun parseResponse(response: Response<MoviesResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got Movie response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
            }
            maxPages = response.body()?.totalPages!!
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseMovieDetailsResponse(response: Response<MovieDetailsResponse>): Resource<MovieDetailsResponse> {
        return if (response.isSuccessful) {
            Timber.d("Got Movie Details ${response.body()}")
            var result: Resource<MovieDetailsResponse> = Resource.loading(null)
            response.body()?.let {
                result = Resource.success(it)

                val castList: MutableList<CreditsItem> = mutableListOf()
                val crewList: MutableList<CreditsItem> = mutableListOf()
                (it.credits?.cast?.sortedBy { movieCast -> movieCast.creditID })?.forEach { movieCast ->
                    castList.add(
                        CreditsItem(
                            movieCast.id,
                            movieCast.name,
                            movieCast.profilePath,
                            movieCast.popularity,
                            movieCast.adult,
                            character = movieCast.character
                        )
                    )
                }
                (it.credits?.crew?.sortedBy { movieCrew -> movieCrew.creditID })?.forEach { movieCrew ->
                    if (!movieCrew.isContains(crewList))
                        crewList.add(
                            CreditsItem(
                                movieCrew.id,
                                movieCrew.name,
                                movieCrew.profilePath,
                                movieCrew.popularity,
                                movieCrew.adult,
                                job = movieCrew.job
                            )
                        )
                }

                castListStore = Resource.success(castList)
                crewListStore = Resource.success(crewList)
            }
            result
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseAccountState(response: Response<AccountStateResponse>): Boolean {
        return if (response.isSuccessful) {
            Timber.d("Got Movie AccountState ${response.body()}")
            var result = false
            response.body()?.favorite?.let {
                result = it
            }
            result
        } else {
            Timber.d("AccountState error ${response.message()}")
            false
        }
    }
}