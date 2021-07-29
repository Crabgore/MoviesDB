package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.tv.repositories.TVDetailsRepository
import com.crabgore.moviesDB.data.tv.repositories.TVRepository
import org.koin.dsl.module

val tvRepositoryModule =
    module {
        single { TVRepository(get()) }
    }

val tvDetailsRepositoryModule =
    module {
        single { TVDetailsRepository(get()) }
    }