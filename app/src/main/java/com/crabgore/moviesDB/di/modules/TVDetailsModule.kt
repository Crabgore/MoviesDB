package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.tv.details.TVDetailsFragment
import com.crabgore.moviesDB.ui.tv.details.TVDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TVDetailsModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun tvDetailsFragment(): TVDetailsFragment

    @Binds
    @IntoMap
    @ViewModelKey(TVDetailsViewModel::class)
    abstract fun bindViewModel(viewModel: TVDetailsViewModel): ViewModel
}