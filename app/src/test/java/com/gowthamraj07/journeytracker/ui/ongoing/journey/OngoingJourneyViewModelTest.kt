package com.gowthamraj07.journeytracker.ui.ongoing.journey

import com.gowthamraj07.journeytracker.domain.usecase.LoadPlacesUseCase
import io.kotest.core.spec.style.StringSpec
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OngoingJourneyViewModelTest: StringSpec({

    val loadPlacesUseCase = mockk<LoadPlacesUseCase>(relaxed = true)
    val viewModel = OngoingJourneyViewModel(loadPlacesUseCase)

    val testDispatcher = UnconfinedTestDispatcher()

    beforeAny{
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    "trigger load places from use case" {
        viewModel.loadPlaces()

        coVerify {
            loadPlacesUseCase.execute()
        }
    }
})