package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.tv.repositories.TVDetailsRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.TVRepository
import com.crabgore.moviesDB.data.tv.repositories.TVRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.TVDetailsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TVRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideTVRepository(repository: TVRepositoryImpl): TVRepository

    @Binds
    @Singleton
    abstract fun provideTVDetailsRepository(repository: TVDetailsRepositoryImpl): TVDetailsRepository
}