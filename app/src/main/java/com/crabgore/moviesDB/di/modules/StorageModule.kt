package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.storage.SharedPreferencesStorage
import com.crabgore.moviesDB.domain.storage.Storage
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}