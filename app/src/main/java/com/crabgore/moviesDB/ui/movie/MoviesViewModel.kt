package com.crabgore.moviesDB.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.data.MoviesResponse
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MoviesViewModel @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    private val _nowPlayingState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _popularState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _topRatedState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    private val _upcomingState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val nowPlayingState = _nowPlayingState
    val popularState = _popularState
    val topRatedState = _topRatedState
    val upcomingState = _upcomingState

    fun getData() {
        val job = viewModelScope.launch {
            Timber.d("Getting Now Playing Movies")
            val nowPlayingResponse = remote.getNowPlayingMovies(null)
            parseResponse(nowPlayingResponse, _nowPlayingState)

            Timber.d("Getting Popular")
            val popularResponse = remote.getPopularMovies(null)
            parseResponse(popularResponse, _popularState)

            Timber.d("Getting Top Rated")
            val topRatedResponse = remote.getTopRatedMovies(null)
            parseResponse(topRatedResponse, _topRatedState)

            Timber.d("Getting Upcoming")
            val upcomingResponse = remote.getUpcomingMovies(null)
            parseResponse(upcomingResponse, _upcomingState)
        }
        addJod(job)
    }

    private fun parseResponse(response: Response<MoviesResponse>, state: MutableStateFlow<Resource<List<MovieItem>>>) {
        if (response.isSuccessful) {
            Timber.d("Got Movie response ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.title, it.posterPath, it.voteAverage, it.adult))
            }
            state.value = Resource.success(list)
        } else state.value = Resource.error(data = null, message = response.message())
    }
}