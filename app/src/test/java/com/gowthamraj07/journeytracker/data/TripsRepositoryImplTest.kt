package com.gowthamraj07.journeytracker.data

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class TripsRepositoryImplTest: StringSpec({

    val tripDao = mockk<TripDao> {
        every { getTrips() } returns flowOf(emptyList())
    }
    val tripsRepository = TripsRepositoryImpl(tripDao)

    "should trigger getTrips in tripDao" {
        tripsRepository.getTrips()

        verify {
            tripDao.getTrips()
        }
    }
})