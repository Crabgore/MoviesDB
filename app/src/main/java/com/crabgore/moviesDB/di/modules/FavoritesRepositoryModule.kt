package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.favorites.repositories.FavoritesRepository
import org.koin.dsl.module

val favoritesRepositoryModule =
    module {
        single { FavoritesRepository(get(), get()) }
    }