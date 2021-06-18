package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.repositories.interfaces.PeopleRepository
import com.crabgore.moviesDB.domain.repositories.PeopleRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class PeopleRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMoviesRepository(repository: PeopleRepositoryImpl): PeopleRepository
}