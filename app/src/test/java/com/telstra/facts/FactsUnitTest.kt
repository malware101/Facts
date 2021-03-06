package com.telstra.facts

import android.os.Bundle
import com.telstra.facts.arch.viewmodel.testSchedulers
import com.telstra.facts.integration.FactsRepository
import com.telstra.facts.manager.FactsManager
import com.telstra.facts.viewmodel.FactsListViewModel
import com.telstra.facts.viewmodel.FactsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FactsUnitTest {

    private lateinit var viewModel: FactsListViewModel

    @MockK
    private lateinit var factsUseCase: FactsUseCase

    @MockK
    private lateinit var factsManager: FactsManager

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)

        factsUseCase = spyk(
            FactsUseCase(
                factsManager,
                testSchedulers
            )
        )

        viewModel = FactsListViewModel(factsUseCase)
    }

    // State restoration
    @org.junit.jupiter.api.Test
    fun `state is stored on facts list screen`() {
        every { viewModel.data.value?.title } returns "About Canada"
        val bundle: Bundle = mockk()
        val fakeBundle = mutableMapOf<String, Any>()
        val FACT_ITEM_TITLE = "fact_item_title"

        with(bundle) {
            every { putString(FACT_ITEM_TITLE, any()) } answers {
                fakeBundle[FACT_ITEM_TITLE] = secondArg<String>()
            }
        }

        Assert.assertEquals(0, fakeBundle.size)
        viewModel.saveState(bundle)
        // checks correct values are stored
        Assert.assertEquals("About Canada", fakeBundle[FACT_ITEM_TITLE])
    }

    @org.junit.jupiter.api.Test
    fun `state is restored on facts list screen`() {
        every { viewModel.data.value?.title } returns "About Canada"
        val bundle: Bundle = mockk()
        val FACT_ITEM_TITLE = "fact_item_title"

        val fakeBundle = mapOf(FACT_ITEM_TITLE to "About Canada")

        with(bundle) {
            every { getString(any()) } answers { fakeBundle[firstArg()] as String }
        }

        // mimics restart activity
        viewModel.restoreState(bundle)

        Assert.assertEquals(viewModel.data.value?.title, "About Canada")
    }
}
