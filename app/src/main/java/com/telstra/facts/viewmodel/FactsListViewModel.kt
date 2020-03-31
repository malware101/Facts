package com.telstra.facts.viewmodel

import com.telstra.facts.arch.viewmodel.SingleUseCaseViewModel
import com.telstra.facts.model.Facts
import javax.inject.Inject

class FactsListViewModel @Inject constructor(factsUseCase: FactsUseCase) :
    SingleUseCaseViewModel<Unit, Facts>(factsUseCase) {
    fun load(params: Unit) =
        submit(params)
}