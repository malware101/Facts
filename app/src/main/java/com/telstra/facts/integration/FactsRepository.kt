package com.telstra.facts.integration

import com.telstra.facts.model.Facts
import io.reactivex.Observable

interface FactsRepository {
    fun fetchFacts(): Observable<Facts>
}

class FactsRepositoryImpl(private val factsService: FactsService) : FactsRepository {

    companion object {
        private const val FACTS_URL =
            "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"
    }

    override fun fetchFacts(): Observable<Facts> = factsService.getFacts(FACTS_URL)
}