package com.telstra.facts.arch.di

import com.telstra.facts.arch.NetworkModule
import com.telstra.facts.di.FactsClientBindingModule
import dagger.Module

@Module(
    includes = [
        NetworkModule::class,
        DesignSingletonModule::class,
        FactsClientBindingModule::class
    ]
)
class AppModules
