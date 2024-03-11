package com.gowthamraj07.journeytracker.data

import app.cash.turbine.test
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripsWithPlaces
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class TripsRepositoryImplTest : StringSpec({

    val tripDao = mockk<TripDao> {
        every { getTripsWithPlaces() } returns flowOf(emptyList())
    }

    val tripsRepository = TripsRepositoryImpl(tripDao)

    val testDispatcher = UnconfinedTestDispatcher()

    beforeAny {
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    "should trigger getTrips in tripDao" {
        tripsRepository.getTrips()

        verify {
            tripDao.getTripsWithPlaces()
        }
    }

    "should return the flow of trips domain from the flow of tripsWithPlaces entities" {
        every { tripDao.getTripsWithPlaces() } returns flowOf(
            listOf(
                TripsWithPlaces(
                    TripEntity(id = 1, name = "Trip to beach"),
                    listOf(PlaceEntity(
                        id = 1,
                        tripId = 1,
                        latitude = 12.0,
                        longitude = 12.0,
                        imageUrl = "https://live.staticflickr.com/65535/53573430851_3005ebe97a.jpg"
                    ))
                )
            )
        )

        tripsRepository.getTrips().test {
            awaitItem() shouldBe listOf(
                Trip(
                    id = 1,
                    name = "Trip to beach",
                    image = TripImage.RemoteImage(
                        "https://live.staticflickr.com/65535/53573430851_3005ebe97a.jpg"
                    )
                )
            )

            cancelAndIgnoreRemainingEvents()
        }

    }
})