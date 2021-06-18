package com.crabgore.moviesDB.ui.tv.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVCategoryViewModel @Inject constructor(
    private val context: Context,
    private val repository: TVRepository
) : BaseViewModel() {
    private val _tvState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val tvState = _tvState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getData(command: String, page: Int) {
        Timber.d("Getting TV category page:$page")
        if (page >= repository.maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = when (command) {
                context.getString(R.string.on_the_air) -> viewModelScope.launch {
                    _tvState.value = repository.getOnTheAirTVs(page)
                }

                context.getString(R.string.popular) -> viewModelScope.launch {
                    _tvState.value = repository.getPopularTVs(page)
                }

                context.getString(R.string.top_rating) -> viewModelScope.launch {
                    _tvState.value = repository.getTopRatedTVs(page)
                }

                else -> viewModelScope.launch {
                    _tvState.value = repository.getFavoriteTVs(page)
                }
            }

            addJod(job)
        }
    }
}