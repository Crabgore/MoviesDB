package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.people.repositories.PeopleDetailsRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleRepository
import com.crabgore.moviesDB.data.people.repositories.PeopleRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleDetailsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class PeopleRepositoryModule {

    @Binds
    @Singleton
    abstract fun providePeopleRepository(repository: PeopleRepositoryImpl): PeopleRepository

    @Binds
    @Singleton
    abstract fun providePeopleDetailsRepository(repository: PeopleDetailsRepositoryImpl): PeopleDetailsRepository
}