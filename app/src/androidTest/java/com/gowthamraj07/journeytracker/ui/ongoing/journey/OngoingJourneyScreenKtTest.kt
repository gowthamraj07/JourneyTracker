package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.compose.ui.test.junit4.createComposeRule
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class OngoingJourneyScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<OngoingJourneyViewModel>{
        coEvery { loadPlacesFor(any()) } returns Unit
        coEvery { places } returns MutableStateFlow(OngoingJourneyUIState.Loading)
    }

    @Test
    fun triggerViewModelMethod_toLoadPlaces() {
        val tripId = 1L
        with(composeTestRule) {
            setContent {
                OngoingJourneyScreen(
                    tripId = tripId,
                    viewModel = viewModel,
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        verify {
            viewModel.loadPlacesFor(tripId)
        }
    }
}