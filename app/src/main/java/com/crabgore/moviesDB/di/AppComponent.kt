package com.crabgore.moviesDB.di

import android.content.Context
import com.crabgore.moviesDB.App
import com.crabgore.moviesDB.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, RemoteModule::class, MoviesFragmentModule::class,
        MovieDetailsModule::class, MoviesCategoryModule::class, TVFragmentModule::class,
        TVDetailsModule::class, TVCategoryModule::class, PeopleFragmentModule::class,
        PeopleDetailsModule::class, SearchModule::class]
)

interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}