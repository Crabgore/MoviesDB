package com.crabgore.moviesDB.ui.people.details

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.common.isContains
import com.crabgore.moviesDB.data.PeopleDetailsResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class PeopleDetailsViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    private val _peopleState = MutableStateFlow<Resource<PeopleDetailsResponse>>(Resource.loading(null))
    private val _movieCastState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _tvCastState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _movieCrewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _tvCrewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    val peopleState = _peopleState
    val movieCastState = _movieCastState
    val tvCastState = _tvCastState
    val movieCrewState = _movieCrewState
    val tvCrewState = _tvCrewState

    fun getData(id: Int) {
        Timber.d("Getting people Details $id")
        val job = viewModelScope.launch {
            Timber.d("Getting Movie Details $id")
            val detailsResponse = remote.getPeopleDetails(id)
            parsePeopleDetailsResponse(detailsResponse)
        }
        addJod(job)
    }

    private fun parsePeopleDetailsResponse(response: Response<PeopleDetailsResponse>) {
        Timber.d("Got people Details ${response.body()}")
        if (response.isSuccessful) {
            response.body()?.let {
                _peopleState.value = Resource.success(it)

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
                _movieCastState.value = Resource.success(movieCastList)
                _movieCrewState.value = Resource.success(movieCrewList)

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
                _tvCastState.value = Resource.success(tvCastList)
                _tvCrewState.value = Resource.success(tvCrewList)
            }
        } else _peopleState.value = Resource.error(data = null, message = response.message())
    }
}