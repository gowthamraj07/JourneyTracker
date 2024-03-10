package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

class GetTripsUseCaseTest: StringSpec({

    val tripsRepository = mockk<TripsRepository> {
        every { getTrips() } returns flowOf(emptyList())
    }
    val getTripsUseCase = GetTripsUseCase(tripsRepository)

    "should trigger repository to get trips" {
        getTripsUseCase.execute()

        verify { tripsRepository.getTrips() }
    }
})