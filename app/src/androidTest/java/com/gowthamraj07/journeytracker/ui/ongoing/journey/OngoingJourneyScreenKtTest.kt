package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.compose.ui.test.junit4.createComposeRule
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class OngoingJourneyScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<OngoingJourneyViewModel>(relaxed = true)

    @Test
    fun triggerViewModelMethod_toLoadPlaces() {
        with(composeTestRule) {
            setContent {
                OngoingJourneyScreen(
                    tripId = 1,
                    viewModel = viewModel,
                    navigator = EmptyDestinationsNavigator
                )
            }
        }

        verify {
            viewModel.loadPlacesFor()
        }
    }
}