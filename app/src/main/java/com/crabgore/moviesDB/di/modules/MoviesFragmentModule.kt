package com.crabgore.moviesDB.di.modules

import androidx.lifecycle.ViewModel
import com.crabgore.moviesDB.di.ViewModelBuilder
import com.crabgore.moviesDB.di.ViewModelKey
import com.crabgore.moviesDB.ui.movie.MoviesFragment
import com.crabgore.moviesDB.ui.movie.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MoviesFragmentModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun moviesFragment(): MoviesFragment

    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindViewModel(viewModel: MoviesViewModel): ViewModel
}