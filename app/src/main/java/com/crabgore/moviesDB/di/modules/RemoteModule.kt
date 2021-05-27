package com.crabgore.moviesDB.di.modules

import com.crabgore.moviesDB.domain.Remote
import com.crabgore.moviesDB.domain.TMDBRemote
import dagger.Binds
import dagger.Module

@Module
abstract class RemoteModule {

    @Binds
    abstract fun provideRemote(download: TMDBRemote): Remote
}