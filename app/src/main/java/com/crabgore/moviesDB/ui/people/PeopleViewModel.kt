package com.crabgore.moviesDB.ui.people

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.people.repositories.PeopleRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.PeopleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class PeopleViewModel(
    private val repository: PeopleRepository
) : BaseViewModel() {
    private val _peopleState = MutableStateFlow<Resource<List<PeopleItem>>>(Resource.Loading(null))
    val peopleState = _peopleState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getData(page: Int) {
        Timber.d("Getting Popular People page: $page")
        if (page >= repository.maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = viewModelScope.launch {
                _peopleState.value = repository.getPopularPeople(page)
            }
            addJod(job)
        }
    }
}