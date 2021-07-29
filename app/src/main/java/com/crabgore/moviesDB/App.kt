package com.crabgore.moviesDB

import android.app.Application
import com.crabgore.moviesDB.di.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        /**
         * Сажаем Timber
         */
        Timber.plant(Timber.DebugTree())

        /**
         * Стартуем Koin
         */
        startKoin {

            //inject Android context
            androidContext(this@App)

            // declare modules
            modules(
                listOf(
                    favoritesRepositoryModule,
                    movieDetailsModule,
                    moviesCategoryModule,
                    moviesFragmentModule,
                    moviesRepositoryModule,
                    movieDetailsRepositoryModule,
                    peopleDetailsModule,
                    peopleFragmentModule,
                    peopleRepositoryModule,
                    peopleDetailsRepositoryModule,
                    moviesApiModule,
                    tvsApiModule,
                    peopleApiModule,
                    movieDetailsApiModule,
                    tvDetailsApiModule,
                    peopleDetailsApiModule,
                    favoritesApiModule,
                    searchApiModule,
                    userApiModule,
                    userDetailsApiModule,
                    searchModule,
                    searchRepositoryModule,
                    storageModule,
                    tvCategoryModule,
                    tvDetailsModule,
                    tvFragmentModule,
                    tvRepositoryModule,
                    tvDetailsRepositoryModule,
                    userModule,
                    userRepositoryModule,
                    userDetailsRepositoryModule
                )
            )
        }
    }
}