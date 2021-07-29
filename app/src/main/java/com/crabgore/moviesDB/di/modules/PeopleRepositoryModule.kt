package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.people.repositories.PeopleDetailsRepository
import com.crabgore.moviesDB.data.people.repositories.PeopleRepository
import org.koin.dsl.module

val peopleRepositoryModule =
    module {
        single { PeopleRepository(get()) }
    }

val peopleDetailsRepositoryModule =
    module {
        single { PeopleDetailsRepository(get()) }
    }