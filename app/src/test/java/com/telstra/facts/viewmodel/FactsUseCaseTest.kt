package com.telstra.facts.viewmodel

import com.telstra.facts.arch.viewmodel.testSchedulers
import com.telstra.facts.integration.FactsRepository
import com.telstra.facts.model.Fact
import com.telstra.facts.viewmodel.FactsUseCase
import io.mockk.every
import io.reactivex.Observable
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test

internal class FactsUseCaseTest {

    @MockK
    private lateinit var repository: FactsRepository

    private val useCase: FactsUseCase by lazy {
        FactsUseCase(
            repository,
            testSchedulers
        )
    }

    @Test
    fun fetchFactsListTest() {
        every {
            repository.fetchFacts()
        } returns Observable.empty()
        val testObserver = useCase.observable(Unit).test()
        testObserver.assertComplete()
        testObserver.assertValue { result ->
            result == listOf<Fact>()
        }
    }
}