package com.crabgore.moviesDB.ui.movie.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.favorites.repositories.FavoritesRepository
import com.crabgore.moviesDB.data.movies.repositories.MoviesRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
class MoviesCategoryViewModel(
    private val context: Context,
    private val moviesRepository: MoviesRepository,
    private val favoritesRepository: FavoritesRepository
) : BaseViewModel() {
    private val _moviesState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.Loading(null))
    val moviesState = _moviesState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getData(command: String, page: Int) {
        Timber.d("Getting Movies category page:$page")
        if (page >= moviesRepository.maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = when (command) {
                context.getString(R.string.now_playing) -> viewModelScope.launch {
                    _moviesState.value = moviesRepository.getNowPlayingMovies(page)
                }

                context.getString(R.string.popular) -> viewModelScope.launch {
                    _moviesState.value = moviesRepository.getPopularMovies(page)
                }

                context.getString(R.string.top_rating) -> viewModelScope.launch {
                    _moviesState.value = moviesRepository.getTopRatedMovies(page)
                }

                context.getString(R.string.upcoming) -> viewModelScope.launch {
                    _moviesState.value = moviesRepository.getUpcomingMovies(page)
                }

                else -> viewModelScope.launch {
                    _moviesState.value = favoritesRepository.getFavoriteMovies(page)
                }
            }

            addJod(job)
        }
    }
}