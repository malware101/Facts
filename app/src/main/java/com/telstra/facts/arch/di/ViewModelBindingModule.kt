package com.telstra.facts.arch.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.telstra.facts.arch.viewmodel.MainViewModel
import com.telstra.facts.arch.viewmodel.ViewModelFactory
import com.telstra.facts.arch.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        AppProvideViewModelModule::class
    ]
)
abstract class ViewModelBindingModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindInitializerViewModel(viewModel: MainViewModel): ViewModel
}