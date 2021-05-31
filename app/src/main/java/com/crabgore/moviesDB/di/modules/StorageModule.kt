package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.SharedPreferencesStorage
import com.crabgore.moviesDB.domain.Storage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {

    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}