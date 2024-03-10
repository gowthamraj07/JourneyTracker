package com.gowthamraj07.journeytracker.ui.trips

import androidx.compose.ui.test.junit4.createComposeRule
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class TripsScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val tripsViewModel = mockk<TripsViewModel>{
        every { loadTrips() } returns Unit
        every { state } returns MutableStateFlow(TripsUiState.Empty)
    }

    @Test
    fun triggerViewModelMethod_toLoadTrips() {
        with(composeTestRule) {
            setContent {
                TripsScreen(tripsViewModel, EmptyDestinationsNavigator)
            }
        }

        verify { tripsViewModel.loadTrips() }
    }
}