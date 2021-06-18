package com.crabgore.moviesDB.ui.movie.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.MoviesRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class MoviesCategoryViewModel @Inject constructor(
    private val context: Context,
    private val repository: MoviesRepository
) : BaseViewModel() {
    private val _moviesState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val moviesState = _moviesState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getData(command: String, page: Int) {
        Timber.d("Getting Movies category page:$page")
        if (page >= repository.maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = when (command) {
                context.getString(R.string.now_playing) -> viewModelScope.launch {
                    _moviesState.value = repository.getNowPlayingMovies(page)
                }

                context.getString(R.string.popular) -> viewModelScope.launch {
                    _moviesState.value = repository.getPopularMovies(page)
                }

                context.getString(R.string.top_rating) -> viewModelScope.launch {
                    _moviesState.value = repository.getTopRatedMovies(page)
                }

                context.getString(R.string.upcoming) -> viewModelScope.launch {
                    _moviesState.value = repository.getUpcomingMovies(page)
                }

                else -> viewModelScope.launch {
                    _moviesState.value = repository.getFavoriteMovies(page)
                }
            }

            addJod(job)
        }
    }
}