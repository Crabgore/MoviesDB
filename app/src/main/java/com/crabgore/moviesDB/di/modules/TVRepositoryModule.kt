package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.domain.repositories.TVRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TVRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMoviesRepository(repository: TVRepositoryImpl): TVRepository
}