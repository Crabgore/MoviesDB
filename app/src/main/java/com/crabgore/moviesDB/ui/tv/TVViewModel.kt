package com.crabgore.moviesDB.ui.tv

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVViewModel @Inject constructor(
    private val repository: TVRepository
) : BaseViewModel() {
    private val _onTheAirState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _popularState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _topRatedState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val onTheAirState = _onTheAirState
    val popularState = _popularState
    val topRatedState = _topRatedState

    fun getData() {
        val job = viewModelScope.launch {
            Timber.d("Getting On The Air TVs")
            _onTheAirState.value = repository.getOnTheAirTVs(null)

            Timber.d("Getting Popular TVs")
            _popularState.value = repository.getPopularTVs(null)

            Timber.d("Getting Top Rated TVs")
            _topRatedState.value = repository.getTopRatedTVs(null)
        }
        addJod(job)
    }
}