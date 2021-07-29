package com.crabgore.moviesDB.data.people.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.people.models.PeopleDetailsResponse
import com.crabgore.moviesDB.data.people.services.PeopleDetailsService
import com.crabgore.moviesDB.ui.items.CreditsItem
import retrofit2.Response
import timber.log.Timber

class PeopleDetailsRepository(
    private val api: PeopleDetailsService
) {
    var movieCastListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    var movieCrewListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    var tvCastListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    var tvCrewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    val append = "movie_credits,tv_credits"

    suspend fun getPeopleDetails(id: Int): Resource<PeopleDetailsResponse> {
        val response = api.peopleDetails(id, API_KEY, getLanguage(), append)
        return parsePeopleDetailsResponse(response)
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