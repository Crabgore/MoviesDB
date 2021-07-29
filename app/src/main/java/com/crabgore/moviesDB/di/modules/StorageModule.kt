package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.storage.Storage
import org.koin.dsl.module

val storageModule =
    module {
        single { Storage(get()) }
    }