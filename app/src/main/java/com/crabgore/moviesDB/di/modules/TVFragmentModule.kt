package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.tv.TVFragment
import com.crabgore.moviesDB.ui.tv.TVViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TVFragmentModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun tvFragment(): TVFragment

    @Binds
    @IntoMap
    @ViewModelKey(TVViewModel::class)
    abstract fun bindViewModel(viewModel: TVViewModel): ViewModel
}