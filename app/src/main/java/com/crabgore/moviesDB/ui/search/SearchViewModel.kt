package com.crabgore.moviesDB.ui.search

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_MOVIE
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SEARCH_TV
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.SearchRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.SearchItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : BaseViewModel() {
    private val _searchState = MutableStateFlow<Resource<List<SearchItem>>>(Resource.loading(null))
    val searchState = _searchState

    fun search(searchId: String, query: String) {
        Timber.d("Getting Search Result")
        val job = when (searchId) {
            SEARCH_MOVIE -> viewModelScope.launch {
                _searchState.value = repository.getSearchMovieResults(query)
            }
            SEARCH_TV -> viewModelScope.launch {
                _searchState.value = repository.getSearchTVResults(query)
            }
            else -> viewModelScope.launch {
                _searchState.value = repository.getSearchPeopleResults(query)
            }
        }
        addJod(job)
    }
}