package com.gowthamraj07.journeytracker.ui.ongoing.journey

import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.usecase.LoadPlacesUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OngoingJourneyViewModelTest : StringSpec({
    val testDispatcher = UnconfinedTestDispatcher()

    val loadPlacesUseCase = mockk<LoadPlacesUseCase>(relaxed = true)
    val viewModel = OngoingJourneyViewModel(loadPlacesUseCase, testDispatcher)


    beforeAny {
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    "trigger load places from use case" {
        viewModel.loadPlacesFor()

        coVerify {
            loadPlacesUseCase.execute()
        }
    }

    "update places with values from loadPlacesUseCase" {
        val placesFlow = flowOf(
            listOf(
                Place(
                    id = 1,
                    tripId = 1,
                    latitude = 12.0,
                    longitude = 13.0,
                    imageUrl = "https://example.com/image.jpg"
                ),
            )
        )
        coEvery { loadPlacesUseCase.execute() } returns placesFlow

        viewModel.loadPlacesFor()

        viewModel.places.value shouldBe OngoingJourneyUIState.Data(placesFlow)
    }
})