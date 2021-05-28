package com.crabgore.moviesDB.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable by lazy { CompositeDisposable() }
    val doneLD: MutableLiveData<Int> = MutableLiveData(0)

    val failure: MutableLiveData<Throwable> = MutableLiveData()

    fun addDisposable(disposable: Disposable) = compositeDisposable.add(disposable)

    fun increaseCounter() {
        doneLD.value = doneLD.value?.plus(1)
    }

    protected fun handleFailure(fail: Throwable) {
        failure.value = fail
    }

    override fun onCleared() = compositeDisposable.clear()
}