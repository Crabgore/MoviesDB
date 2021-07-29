package com.crabgore.moviesDB.ui.tv.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.common.Resource
import com.crabgore.moviesDB.data.favorites.repositories.FavoritesRepository
import com.crabgore.moviesDB.data.tv.repositories.TVRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
class TVCategoryViewModel(
    private val context: Context,
    private val tvRepository: TVRepository,
    private val favoritesRepository: FavoritesRepository
) : BaseViewModel() {
    private val _tvState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val tvState = _tvState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getData(command: String, page: Int) {
        Timber.d("Getting TV category page:$page")
        if (page >= tvRepository.maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = when (command) {
                context.getString(R.string.on_the_air) -> viewModelScope.launch {
                    _tvState.value = tvRepository.getOnTheAirTVs(page)
                }

                context.getString(R.string.popular) -> viewModelScope.launch {
                    _tvState.value = tvRepository.getPopularTVs(page)
                }

                context.getString(R.string.top_rating) -> viewModelScope.launch {
                    _tvState.value = tvRepository.getTopRatedTVs(page)
                }

                else -> viewModelScope.launch {
                    _tvState.value = favoritesRepository.getFavoriteTVs(page)
                }
            }

            addJod(job)
        }
    }
}