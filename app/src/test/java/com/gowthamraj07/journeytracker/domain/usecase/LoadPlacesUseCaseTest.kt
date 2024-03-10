package com.gowthamraj07.journeytracker.domain.usecase

import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

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

    "return places from repository" {
        // Arrange
        val tripId = 1
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
        coEvery { placesRepository.loadPlacesFor(tripId) } returns placesFlow

        // Act
        val result = loadPlacesUseCase.execute(tripId)

        // Assert
        result shouldBe placesFlow
    }
})