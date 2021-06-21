package com.crabgore.moviesDB.ui.people

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.PeopleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class PeopleViewModel  @Inject constructor(
    private val repository: PeopleRepository
) : BaseViewModel() {
    private val _peopleState = MutableStateFlow<Resource<List<PeopleItem>>>(Resource.loading(null))
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