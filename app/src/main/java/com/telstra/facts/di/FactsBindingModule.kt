package com.telstra.facts.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.telstra.facts.ui.FactsListFragment
import com.telstra.facts.viewmodel.FactsListViewModel
import com.telstra.facts.viewmodel.FactsUseCase
import com.westfield.common.viewmodel.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        FactsBindingModule.ProvideViewModel::class
    ]
)
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

    @Module
    class ProvideViewModel {
        @Provides
        @IntoMap
        @ViewModelKey(FactsListViewModel::class)
        internal fun provideFactsListViewModel(factsUseCase: FactsUseCase): ViewModel = FactsListViewModel(factsUseCase)

    }
}
