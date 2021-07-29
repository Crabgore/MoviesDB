package com.crabgore.moviesDB.ui.people.details

import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.people.models.PeopleDetailsResponse
import com.crabgore.moviesDB.data.people.repositories.PeopleDetailsRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.CreditsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PeopleDetailsViewModel(
    private val repository: PeopleDetailsRepository,
) : BaseViewModel() {
    private val _peopleState = MutableStateFlow<Resource<PeopleDetailsResponse>>(Resource.loading(null))
    private val _movieCastState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _movieCrewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _tvCastState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    private val _tvCrewState = MutableStateFlow<Resource<List<CreditsItem>>>(Resource.loading(null))
    val peopleState = _peopleState
    val movieCastState = _movieCastState
    val movieCrewState = _movieCrewState
    val tvCastState = _tvCastState
    val tvCrewState = _tvCrewState

    fun getData(id: Int) {
        val job = viewModelScope.launch {
            Timber.d("Getting people Details $id")
            _peopleState.value = repository.getPeopleDetails(id)
            _movieCastState.value = repository.movieCastListStore
            _movieCrewState.value = repository.movieCrewListStore
            _tvCastState.value = repository.tvCastListStore
            _tvCrewState.value = repository.tvCrewListStore
        }
        addJod(job)
    }
}