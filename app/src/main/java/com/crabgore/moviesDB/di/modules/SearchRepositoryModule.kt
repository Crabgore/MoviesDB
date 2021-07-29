package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.search.repositories.SearchRepository
import org.koin.dsl.module

val searchRepositoryModule =
    module {
        single { SearchRepository(get(), get()) }
    }