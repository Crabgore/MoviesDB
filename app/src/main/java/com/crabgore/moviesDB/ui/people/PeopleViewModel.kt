package com.crabgore.moviesDB.ui.people

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.crabgore.moviesDB.common.parseError
import com.crabgore.moviesDB.data.PeopleResponse
import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.ui.base.BaseViewModel
import com.crabgore.moviesDB.ui.items.PeopleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
class PeopleViewModel  @Inject constructor(
    private val remote: Remote
) : BaseViewModel() {
    val moviesLiveData: MutableLiveData<List<PeopleItem>> = MutableLiveData()
    val isLastPageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var maxPages = Int.MAX_VALUE

    fun getData(page: Int) {
        Timber.d("Getting Popular People page: $page")
        if (page >= maxPages) {
            isLastPageLiveData.value = true
        } else {
            val disposable = remote.getPopularPeople(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(::onError)
                    .subscribe(::parsePopularPeopleResponse, ::handleFailure)

            addDisposable(disposable)
        }
    }

    private fun onError(throwable: Throwable?) {
        Timber.d("Error getting Popular People ${parseError(throwable)}")
    }

    private fun parsePopularPeopleResponse(response: PeopleResponse) {
        Timber.d("Got Popular People $response")
        val list: MutableList<PeopleItem> = mutableListOf()
        response.results.forEach {
            list.add(PeopleItem(it.id, it.name, it.profilePath))
        }
        moviesLiveData.value = list
        maxPages = response.totalPages
        increaseCounter()
    }
}