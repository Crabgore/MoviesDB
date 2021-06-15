package com.crabgore.moviesDB.ui.base

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.common.CompositeJob
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    private val compositeJob by lazy { CompositeJob() }

    fun addJod(job: Job) = compositeJob.add(job)

    override fun onCleared() {
        compositeDisposable.clear()
        compositeJob.cancel()
    }
}