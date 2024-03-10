package com.gowthamraj07.journeytracker.ui.trips

import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class TripsViewModelTest: StringSpec({

    val getTripsUseCase = mockk<GetTripsUseCase>()
    val tripsViewModel = TripsViewModel(getTripsUseCase)

    val mainThreadSurrogate = newSingleThreadContext("UI thread")

    beforeAny {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    afterAny {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    "should trigger the use case to load trips" {
        tripsViewModel.loadTrips()

        verify { getTripsUseCase.execute() }
    }

    "should emit state as Empty when there are no trips" {
        coEvery { getTripsUseCase.execute() } returns flowOf(emptyList())

        tripsViewModel.loadTrips()

        tripsViewModel.state.value shouldBe TripsUiState.Empty
    }
})