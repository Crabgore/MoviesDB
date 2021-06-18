package com.crabgore.moviesDB.domain.repositories

import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.AccountStateResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.TVDetailsResponse
import com.crabgore.moviesDB.data.TVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.MovieItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class TVRepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val storage: Storage
) : BaseRepository(), TVRepository {

    override var maxPages = Int.MAX_VALUE
    override var castListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    override var crewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    override suspend fun getOnTheAirTVs(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getOnTheAirTVs(page) }
        return parseResponse(response)
    }

    override suspend fun getPopularTVs(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getPopularTVs(page) }
        return parseResponse(response)
    }

    override suspend fun getTopRatedTVs(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall { remote.getTopRatedTVs(page) }
        return parseResponse(response)
    }

    override suspend fun getFavoriteTVs(page: Int?): Resource<List<MovieItem>> {
        val response = apiCall {
            remote.getFavoriteTVs(
                storage.getInt(ACCOUNT_ID),
                storage.getString(SESSION_ID)!!,
                page
            )
        }
        return parseResponse(response)
    }

    override suspend fun getTVDetails(id: Int): Resource<TVDetailsResponse> {
        val response = apiCall { remote.getTvDetails(id) }
        return parseTVDetailsResponse(response)
    }

    override suspend fun getTVsAccountState(id: Int): Boolean {
        val response = apiCall { remote.getTVAccountState(id, storage.getString(SESSION_ID)!!) }
        return parseAccountState(response)
    }

    private fun parseResponse(response: Response<TVResponse>): Resource<List<MovieItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
            }
            maxPages = response.body()?.totalPages!!
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parseTVDetailsResponse(response: Response<TVDetailsResponse>): Resource<TVDetailsResponse> {
        return if (response.isSuccessful) {
            Timber.d("Got TV Details ${response.body()}")
            var result: Resource<TVDetailsResponse> = Resource.loading(null)
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
            Timber.d("Got TV AccountState ${response.body()}")
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