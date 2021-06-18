package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.repositories.interfaces.SearchRepository
import com.crabgore.moviesDB.domain.repositories.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class SearchRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMoviesRepository(repository: SearchRepositoryImpl): SearchRepository
}