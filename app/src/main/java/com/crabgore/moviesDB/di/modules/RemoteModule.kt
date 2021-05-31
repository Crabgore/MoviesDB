package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.remote.Remote
import com.crabgore.moviesDB.domain.remote.TMDBRemote
import dagger.Binds
import dagger.Module

@Module
abstract class RemoteModule {

    @Binds
    abstract fun provideRemote(download: TMDBRemote): Remote
}