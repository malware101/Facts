package com.telstra.facts.manager

import com.telstra.facts.integration.FactsRepository
import com.telstra.facts.model.Facts
import io.reactivex.Observable

interface FactsManager {

    fun fetchFacts(): Observable<Facts>
}

internal class FactsManagerImpl(
    private val factsRepository: FactsRepository
) : FactsManager {

    override fun fetchFacts(): Observable<Facts> =
        factsRepository.fetchFacts()
}
