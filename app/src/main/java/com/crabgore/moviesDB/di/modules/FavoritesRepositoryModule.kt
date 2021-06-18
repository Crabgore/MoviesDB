package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.repositories.interfaces.FavoritesRepository
import com.crabgore.moviesDB.domain.repositories.FavoritesRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class FavoritesRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideFavoritesRepository(repository: FavoritesRepositoryImpl): FavoritesRepository
}