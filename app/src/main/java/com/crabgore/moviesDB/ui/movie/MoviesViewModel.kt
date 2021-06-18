package com.crabgore.moviesDB.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.MoviesRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
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
            _nowPlayingState.value = repository.getNowPlayingMovies(null)

            Timber.d("Getting Popular")
            _popularState.value = repository.getPopularMovies(null)

            Timber.d("Getting Top Rated")
            _topRatedState.value = repository.getTopRatedMovies(null)

            Timber.d("Getting Upcoming")
            _upcomingState.value = repository.getUpcomingMovies(null)
        }
        addJod(job)
    }
}