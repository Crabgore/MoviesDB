package com.crabgore.moviesDB.domain.repositories

import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.data.PeopleResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleRepository
import com.crabgore.moviesDB.ui.items.CreditsItem
import com.crabgore.moviesDB.ui.items.PeopleItem
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PeopleRepositoryImpl @Inject constructor(
    private val remote: Remote
) : BaseRepository(), PeopleRepository {

    override var maxPages = Int.MAX_VALUE
    override var movieCastListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    override var movieCrewListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    override var tvCastListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    override var tvCrewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    override suspend fun getPopularPeople(page: Int?): Resource<List<PeopleItem>> {
        val response = apiCall { remote.getPopularPeople(page) }
        return parseResponse(response)
    }

    override suspend fun getPeopleDetails(id: Int): Resource<PeopleDetailsResponse> {
        val response = apiCall { remote.getPeopleDetails(id) }
        return parsePeopleDetailsResponse(response)
    }

    private fun parseResponse(response: Response<PeopleResponse>): Resource<List<PeopleItem>> {
        return if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<PeopleItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(PeopleItem(it.id, it.name, it.profilePath))
            }
            maxPages = response.body()?.totalPages!!
            Resource.success(list)
        } else Resource.error(data = null, message = response.message())
    }

    private fun parsePeopleDetailsResponse(response: Response<PeopleDetailsResponse>): Resource<PeopleDetailsResponse> {
        return if (response.isSuccessful) {
            Timber.d("Got TV Details ${response.body()}")
            var result: Resource<PeopleDetailsResponse> = Resource.loading(null)
            response.body()?.let {
                result = Resource.success(it)

                //parse movie credits
                val movieCastList: MutableList<CreditsItem> = mutableListOf()
                val movieCrewList: MutableList<CreditsItem> = mutableListOf()
                (it.movieCredits?.cast?.sortedByDescending { movieCast -> movieCast.releaseDate })?.forEach { movieCast ->
                    movieCastList.add(
                        CreditsItem(
                            movieCast.id,
                            movieCast.title,
                            movieCast.posterPath,
                            movieCast.voteAverage,
                            movieCast.adult,
                            character = movieCast.character
                        )
                    )
                }
                (it.movieCredits?.crew?.sortedByDescending { movieCrew -> movieCrew.releaseDate })?.forEach { movieCrew ->
                    if (!movieCrew.isContains(movieCrewList))
                        movieCrewList.add(
                            CreditsItem(
                                movieCrew.id,
                                movieCrew.title,
                                movieCrew.posterPath,
                                movieCrew.voteAverage,
                                movieCrew.adult,
                                job = movieCrew.job
                            )
                        )
                }

                movieCastListStore = Resource.success(movieCastList)
                movieCrewListStore = Resource.success(movieCrewList)

                //parse tv credits
                val tvCastList: MutableList<CreditsItem> = mutableListOf()
                val tvCrewList: MutableList<CreditsItem> = mutableListOf()
                (it.tvCredits?.cast?.sortedByDescending { tvCast -> tvCast.firstAirDate })?.forEach { tvCast ->
                    tvCastList.add(
                        CreditsItem(
                            tvCast.id,
                            tvCast.name,
                            tvCast.posterPath,
                            tvCast.voteAverage,
                            null,
                            character = tvCast.character
                        )
                    )
                }
                (it.tvCredits?.crew?.sortedByDescending { tvCrew -> tvCrew.firstAirDate })?.forEach { tvCrew ->
                    if (!tvCrew.isContains(tvCrewList))
                        tvCrewList.add(
                            CreditsItem(
                                tvCrew.id,
                                tvCrew.name,
                                tvCrew.posterPath,
                                tvCrew.voteAverage,
                                null,
                                job = tvCrew.job
                            )
                        )
                }
                tvCastListStore = Resource.success(tvCastList)
                tvCrewListStore = Resource.success(tvCrewList)
            }
            result
        } else Resource.error(data = null, message = response.message())
    }
}