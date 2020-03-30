package com.telstra.facts.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.telstra.facts.ui.FactsListFragment
import com.telstra.facts.viewmodel.FactsListViewModel
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class FactsBindingModule {

    @ContributesAndroidInjector(
        modules = [
            InjectViewModel::class
        ]
    )
    abstract fun bindFactsListFragment(): FactsListFragment

    @Module
    class InjectViewModel {
        @Provides
        internal fun provideInjectFactsListViewModel(
            factory: ViewModelProvider.Factory,
            target: FactsListFragment
        ) = ViewModelProviders.of(target, factory).get(FactsListViewModel::class.java)
    }
}
