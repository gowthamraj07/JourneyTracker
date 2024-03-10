package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.coVerify
import io.mockk.mockk

class LoadPlacesUseCaseTest : StringSpec({

    val placesRepository = mockk<PlacesRepository>(relaxed = true)
    val loadPlacesUseCase = LoadPlacesUseCase(placesRepository)

    "call repository to load places" {
        val tripId = 1
        loadPlacesUseCase.execute(tripId)

        coVerify {
            placesRepository.loadPlacesFor(tripId)
        }
    }
})