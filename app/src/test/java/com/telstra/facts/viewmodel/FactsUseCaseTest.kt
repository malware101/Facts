package com.telstra.facts.viewmodel

import com.telstra.facts.arch.viewmodel.testSchedulers
import com.telstra.facts.manager.FactsManager
import com.telstra.facts.model.Facts
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.junit.jupiter.api.Test

internal class FactsUseCaseTest {

    @MockK
    private lateinit var factsManager: FactsManager

    private val useCase: FactsUseCase by lazy {
        FactsUseCase(
            factsManager,
            testSchedulers
        )
    }

    @Test
    fun fetchFactsListTest() {
        every {
            factsManager.fetchFacts()
        } returns Observable.just(Facts("About Canada", listOf()))
        val testObserver = useCase.observable(Unit).test()
        testObserver.assertComplete()
        testObserver.assertValue { result ->
            result == Facts("About Canada", listOf())
        }
    }
}