package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.movies.repositories.MovieDetailsRepository
import com.crabgore.moviesDB.data.movies.repositories.MoviesRepository
import org.koin.dsl.module

val moviesRepositoryModule =
    module {
        single { MoviesRepository(get(), get()) }
    }

val movieDetailsRepositoryModule =
    module {
        single { MovieDetailsRepository(get()) }
    }