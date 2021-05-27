package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.search.SearchFragment
import com.crabgore.moviesDB.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun searchFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindViewModel(viewModel: SearchViewModel): ViewModel
}