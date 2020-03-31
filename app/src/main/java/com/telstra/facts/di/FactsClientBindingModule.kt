package com.telstra.facts.di

import com.telstra.facts.manager.FactsManager
import com.telstra.facts.manager.FactsManagerImpl
import com.telstra.facts.integration.FactsRepository
import com.telstra.facts.integration.FactsRepositoryImpl
import com.telstra.facts.integration.FactsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class FactsClientBindingModule {

    @Provides
    @Singleton
    fun provideFactsService(retrofit: Retrofit): FactsService =
        retrofit.create(FactsService::class.java)

    @Provides
    @Singleton
    fun provideFactsManager(factsService: FactsService): FactsManager {
        val factsRepository: FactsRepository = FactsRepositoryImpl(factsService)
        return FactsManagerImpl(factsRepository)
    }
}
