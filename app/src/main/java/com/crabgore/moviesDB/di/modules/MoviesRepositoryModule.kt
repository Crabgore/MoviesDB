package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.movies.repositories.MovieDetailsRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.MoviesRepository
import com.crabgore.moviesDB.data.movies.repositories.MoviesRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.MovieDetailsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class MoviesRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMoviesRepository(repository: MoviesRepositoryImpl): MoviesRepository

    @Binds
    @Singleton
    abstract fun provideMovieDetailsRepository(repository: MovieDetailsRepositoryImpl): MovieDetailsRepository
}