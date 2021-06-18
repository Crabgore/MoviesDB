package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.remote.TMDBRemote
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RemoteModule {

    @Binds
    @Singleton
    abstract fun provideRemote(download: TMDBRemote): Remote
}