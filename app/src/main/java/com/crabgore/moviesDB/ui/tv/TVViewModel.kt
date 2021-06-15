package com.crabgore.moviesDB.ui.tv

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.TVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVViewModel @Inject constructor(
    private val remote: Remote
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
            val nowPlayingResponse = remote.getOnTheAirTVs(null)
            parseResponse(nowPlayingResponse, _onTheAirState)

            Timber.d("Getting Popular TVs")
            val popularResponse = remote.getPopularTVs(null)
            parseResponse(popularResponse, _popularState)

            Timber.d("Getting Top Rated TVs")
            val topRatedResponse = remote.getTopRatedTVs(null)
            parseResponse(topRatedResponse, _topRatedState)
        }
        addJod(job)
    }

    private fun parseResponse(response: Response<TVResponse>, state: MutableStateFlow<Resource<List<MovieItem>>>) {
        if (response.isSuccessful) {
            Timber.d("Got TV response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
            }
            state.value = Resource.success(list)
        } else state.value = Resource.error(data = null, message = response.message())
    }
}