package com.crabgore.moviesDB.data.tv.repositories

import com.crabgore.moviesDB.Const
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.common.getLanguage
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.tv.models.TVDetailsResponse
import com.crabgore.moviesDB.data.tv.services.TVDetailsServices
import com.crabgore.moviesDB.ui.items.CreditsItem
import retrofit2.Response
import timber.log.Timber

class TVDetailsRepository(
    private val api: TVDetailsServices
) {
    var castListStore: Resource<List<CreditsItem>> = Resource.loading(null)
    var crewListStore: Resource<List<CreditsItem>> = Resource.loading(null)

    val append = "credits"

    suspend fun getTVDetails(id: Int): Resource<TVDetailsResponse> {
        val response = api.tvDetails(id, Const.Keys.API_KEY, getLanguage(), append)
        return parseTVDetailsResponse(response)
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
}