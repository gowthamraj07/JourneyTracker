package com.gowthamraj07.journeytracker.ui.trips

import com.gowthamraj07.journeytracker.domain.usecase.GetTripsUseCase
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
})