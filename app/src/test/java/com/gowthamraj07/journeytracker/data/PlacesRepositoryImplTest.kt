package com.gowthamraj07.journeytracker.data

import app.cash.turbine.test
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.domain.Place
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
class PlacesRepositoryImplTest: StringSpec({

    val testDispatcher = UnconfinedTestDispatcher()

    val placeDao = mockk<PlaceDao>(relaxed = true)
    val placesRepository = PlacesRepositoryImpl(placeDao, testDispatcher)

    beforeAny{
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    "call placeDao to get places" {
        val tripId = 1L

        placesRepository.loadPlacesFor(tripId).test {
            cancelAndIgnoreRemainingEvents()
            coVerify {
                placeDao.getPlacesByTrip(tripId)
            }
        }
    }


    "return Places domain from PlaceEntities returned from PlacesDao" {
        val tripId = 1L
        val placeEntity = PlaceEntity(
            id = 1,
            tripId = 1,
            latitude = 12.0,
            longitude = 13.0,
            imageUrl = "https://live.staticflickr.com/65535/53573430851_3005ebe97a.jpg",
        )
        coEvery { placeDao.getPlacesByTrip(tripId) } returns flowOf(listOf(placeEntity))

        placesRepository.loadPlacesFor(tripId).test {
            awaitItem() shouldBe listOf(Place(
                id = 1,
                tripId = 1,
                latitude = 12.0,
                longitude = 13.0,
                imageUrl = "https://live.staticflickr.com/65535/53573430851_3005ebe97a.jpg"
            ))

            cancelAndIgnoreRemainingEvents()
        }
    }
})

