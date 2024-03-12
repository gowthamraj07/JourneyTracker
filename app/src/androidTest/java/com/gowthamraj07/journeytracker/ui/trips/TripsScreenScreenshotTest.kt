package com.gowthamraj07.journeytracker.ui.trips

import androidx.compose.ui.test.junit4.createComposeRule
import com.gowthamraj07.journeytracker.R
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.karumi.shot.ScreenshotTest
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class TripsScreenScreenshotTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun emptyState() {
        with(composeTestRule) {
            setContent {
                TripsScreen(
                    viewModel = mockkViewModelWith(MutableStateFlow(TripsUiState.Empty)),
                    navigator = EmptyDestinationsNavigator
                )
            }

            compareScreenshot(this)
        }
    }

    @Test
    fun dataState() {
        with(composeTestRule) {
            setContent {
                TripsScreen(
                    viewModel = mockkViewModelWith(
                        MutableStateFlow(
                            TripsUiState.Data(
                                MutableStateFlow(
                                    listOf(
                                        Trip(
                                            id = 1,
                                            name = "Trip to beach",
                                            image = TripImage.LocalImage(R.drawable.bouncing_circles)
                                        )
                                    )
                                )
                            )
                        )
                    ),
                    navigator = EmptyDestinationsNavigator
                )
            }

            compareScreenshot(this)
        }
    }

    private fun mockkViewModelWith(mutableStateFlow: MutableStateFlow<TripsUiState>): TripsViewModel {
        return mockk<TripsViewModel> {
            every { state } returns mutableStateFlow
            every { loadTrips() } returns Unit
        }
    }
}