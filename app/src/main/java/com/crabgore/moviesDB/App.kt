package com.crabgore.moviesDB

import com.crabgore.moviesDB.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        /**
         * Сажаем Timber
         */
        Timber.plant(Timber.DebugTree())
    }

    /**
     * Запускаем Даггер
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}