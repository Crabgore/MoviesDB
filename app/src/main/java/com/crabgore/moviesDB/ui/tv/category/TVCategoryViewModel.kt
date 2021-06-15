package com.crabgore.moviesDB.ui.tv.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crabgore.moviesDB.Const.MyPreferences.Companion.ACCOUNT_ID
import com.crabgore.moviesDB.Const.MyPreferences.Companion.SESSION_ID
import com.crabgore.moviesDB.R
import com.crabgore.moviesDB.data.Resource
import com.crabgore.moviesDB.data.TVResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.storage.Storage
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.MovieItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class TVCategoryViewModel @Inject constructor(
    private val context: Context,
    private val remote: Remote,
    private val storage: Storage
) : BaseViewModel() {
    private val _tvState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.loading(null))
    val tvState = _tvState
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var maxPages = Int.MAX_VALUE

    fun getData(command: String, page: Int) {
        Timber.d("Getting TV category page:$page")
        if (page >= maxPages) {
            isLastPageLiveData.value = true
        } else {
            val job = when (command) {
                context.getString(R.string.on_the_air) -> viewModelScope.launch {
                    val response = remote.getOnTheAirTVs(page)
                    parseResponse(response)
                }

                context.getString(R.string.popular) -> viewModelScope.launch {
                    val response = remote.getPopularTVs(page)
                    parseResponse(response)
                }

                context.getString(R.string.top_rating) -> viewModelScope.launch {
                    val response = remote.getTopRatedTVs(page)
                    parseResponse(response)
                }

                else -> viewModelScope.launch {
                    val response = remote.getFavoriteTVs(
                        storage.getInt(ACCOUNT_ID),
                        storage.getString(SESSION_ID)!!,
                        page
                    )
                    parseResponse(response)
                }
            }

            addJod(job)
        }
    }

    private fun parseResponse(response: Response<TVResponse>) {
        if (response.isSuccessful) {
            Timber.d("Got TV category ${response.body()}")
            val list: MutableList<MovieItem> = mutableListOf()
            response.body()?.results?.forEach {
                list.add(MovieItem(it.id, it.name, it.posterPath, it.voteAverage, false))
            }
            _tvState.value = Resource.success(list)
            maxPages = response.body()?.totalPages!!
        } else _tvState.value = Resource.error(data = null, message = response.message())
    }
}