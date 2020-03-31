package com.telstra.facts.viewmodel

import com.telstra.facts.manager.FactsManager
import com.telstra.facts.arch.viewmodel.UseCase
import com.telstra.facts.arch.viewmodel.UseCaseSchedulers
import com.telstra.facts.model.Facts
import io.reactivex.Observable
import javax.inject.Inject

class FactsUseCase @Inject constructor(
    private val factsManager: FactsManager,
    schedulers: UseCaseSchedulers
) : UseCase<Unit, Facts>(schedulers) {

    override fun observable(params: Unit): Observable<Facts> =
        factsManager.fetchFacts()

}