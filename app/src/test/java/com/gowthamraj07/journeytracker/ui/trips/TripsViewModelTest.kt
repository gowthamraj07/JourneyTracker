package com.gowthamraj07.journeytracker.ui.trips

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class TripsViewModelTest : StringSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    val getTripsUseCase = mockk<GetTripsUseCase>()
    val tripsViewModel = TripsViewModel(getTripsUseCase)

    beforeAny {
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
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

    "should emit state as Data when there are trips" {
        val tripsFlow = flowOf(
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
        coEvery { getTripsUseCase.execute() } returns tripsFlow

        tripsViewModel.loadTrips()

        tripsViewModel.state.value shouldBe TripsUiState.Data(tripsFlow)
    }
})