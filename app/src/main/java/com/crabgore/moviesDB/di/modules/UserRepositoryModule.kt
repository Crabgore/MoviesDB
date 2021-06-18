package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.repositories.interfaces.UserRepository
import com.crabgore.moviesDB.domain.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideMoviesRepository(repository: UserRepositoryImpl): UserRepository
}