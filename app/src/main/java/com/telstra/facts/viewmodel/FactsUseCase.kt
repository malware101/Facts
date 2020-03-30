package com.telstra.facts.viewmodel

import com.telstra.facts.arch.viewmodel.UseCase
import com.telstra.facts.arch.viewmodel.UseCaseSchedulers
import com.telstra.facts.integration.FactsRepository
import com.telstra.facts.model.Fact
import io.reactivex.Observable

class FactsUseCase constructor(
    protected val factsRepository: FactsRepository,
    schedulers: UseCaseSchedulers
) : UseCase<Unit, List<Fact>>(schedulers) {

    override fun observable(params: Unit): Observable<List<Fact>> =
        factsRepository.fetchFacts().map { it.facts }

}