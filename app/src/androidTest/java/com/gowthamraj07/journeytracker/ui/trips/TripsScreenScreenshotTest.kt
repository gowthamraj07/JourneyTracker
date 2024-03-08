package com.gowthamraj07.journeytracker.ui.trips

import androidx.compose.ui.test.junit4.createComposeRule
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.karumi.shot.ScreenshotTest
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
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
                    uiStateFlow = MutableStateFlow(TripsUiState.Empty),
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
                    uiStateFlow = MutableStateFlow(
                        TripsUiState.Data(
                            MutableStateFlow(
                                listOf(
                                    Trip(
                                        id = 1,
                                        name = "Trip to beach",
                                        image = TripImage.RemoteImage(
                                            url = "https://fastly.picsum.photos/id/2/5000/3333.jpg?hmac=_KDkqQVttXw_nM-RyJfLImIbafFrqLsuGO5YuHqD-qQ"
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
}