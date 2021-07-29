package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.user.repositories.UserDetailsRepository
import com.crabgore.moviesDB.data.user.repositories.UserRepository
import org.koin.dsl.module

val userRepositoryModule =
    module {
        single { UserRepository(get(), get()) }
    }

val userDetailsRepositoryModule =
    module {
        single { UserDetailsRepository(get(), get(), get()) }
    }