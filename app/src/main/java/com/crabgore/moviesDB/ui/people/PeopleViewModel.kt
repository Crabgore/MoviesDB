package com.crabgore.moviesDB.ui.people

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.data.PeopleResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.PeopleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class PeopleViewModel  @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    private val _peopleState = MutableStateFlow<Resource<List<PeopleItem>>>(Resource.loading(null))
    val peopleState = _peopleState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var maxPages = Int.MAX_VALUE

    fun getData(page: Int) {
        Timber.d("Getting Popular People page: $page")
        if (page >= maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = viewModelScope.launch {
                val response = remote.getPopularPeople(page)
                parseResponse(response)
            }
            addJod(job)
        }
    }

    private fun parseResponse(response: Response<PeopleResponse>) {
        if (response.isSuccessful) {
            val list: MutableList<PeopleItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(PeopleItem(it.id, it.name, it.profilePath))
            }
            _peopleState.value = Resource.success(list)
            maxPages = response.body()?.totalPages!!
        } else _peopleState.value = Resource.error(data = null, message = response.message())
    }
}