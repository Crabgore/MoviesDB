package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.data.user.repositories.UserDetailsRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.UserRepository
import com.crabgore.moviesDB.data.user.repositories.UserRepositoryImpl
import com.crabgore.moviesDB.domain.repositories.interfaces.UserDetailsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UserRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideUserDetailsRepository(repository: UserDetailsRepositoryImpl): UserDetailsRepository
}