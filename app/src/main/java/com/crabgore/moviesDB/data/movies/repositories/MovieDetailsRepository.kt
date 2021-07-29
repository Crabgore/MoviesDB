package com.crabgore.moviesDB.data.movies.repositories

import com.crabgore.moviesDB.Const.Keys.Companion.API_KEY
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.movies.models.MovieDetailsResponse
import com.crabgore.moviesDB.data.movies.services.MovieDetailsService
import com.crabgore.moviesDB.ui.items.CreditsItem
import retrofit2.Response
import timber.log.Timber

class MovieDetailsRepository(
    private val api: MovieDetailsService
) {
    var castListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    var crewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    val append = "credits"

    suspend fun getMovieDetails(id: Int): Resource<MovieDetailsResponse> {
        val response = api.movieDetails(id, API_KEY, getLanguage(), append)
        return parseMovieDetailsResponse(response)
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
}