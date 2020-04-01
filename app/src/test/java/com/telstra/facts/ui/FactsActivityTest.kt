package com.telstra.facts.ui

import androidx.navigation.NavController
import com.telstra.facts.R
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FactsActivityTest {

    @MockK
    private lateinit var navigationController: NavController

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        navigationController = mockk()
    }

    @Test
    fun factsActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        navigationController.setGraph(R.navigation.facts_nav_graph)

        // Verify that the NavController’s state points to facts fragment
        Assert.assertEquals(navigationController.currentDestination?.id, R.id.factsListFragment)
    }
}