package com.telstra.facts.arch.di

import com.telstra.facts.arch.BaseFragment
import com.telstra.facts.arch.viewmodel.UseCaseSchedulers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Module
class DesignSingletonModule {
    @Provides
    @Singleton
    fun provideUseCaseSchedulers(): UseCaseSchedulers = object : UseCaseSchedulers {
        override val subscribeScheduler = Schedulers.io()
        override val observeScheduler = AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    fun provideDummy(): BaseFragment.DaggerInjectionWorkaroundField = BaseFragment.DaggerInjectionWorkaroundField()
}
