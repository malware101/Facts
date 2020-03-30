package com.telstra.facts.arch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telstra.facts.viewmodel.FactsListViewModel
import com.telstra.facts.di.FactsViewModelModule
import com.westfield.common.viewmodel.ViewModelFactory
import com.westfield.common.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        FactsViewModelModule::class
    ]
)
abstract class ViewModelBindingModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FactsListViewModel::class)
    abstract fun bindInitializerViewModel(viewModel: FactsListViewModel): ViewModel
}