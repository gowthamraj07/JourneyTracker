package com.gowthamraj07.journeytracker.data

import com.google.gson.JsonObject
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class TripsRepositoryImplTest: StringSpec({

    val tripDao = mockk<TripDao> {
        every { getTripsWithPlaces() } returns flowOf(emptyList())
    }
    val flickrApi = mockk<FlickrApi> {
        coEvery { getLocationDetails(any(), any()) } returns JsonObject()
    }

    val tripsRepository = TripsRepositoryImpl(tripDao, flickrApi)

    "should trigger getTrips in tripDao" {
        tripsRepository.getTrips()

        verify {
            tripDao.getTripsWithPlaces()
        }
    }

    "should get the first place of the every trip and trigger flickr search api to get the details of that place"   {
        every { tripDao.getTripsWithPlaces() }  returns flowOf(listOf(
            TripsWithPlaces(
                TripEntity(id = 1, name = "Trip to beach"),
                listOf(PlaceEntity(id = 1, tripId = 1, latitude = 12.0, longitude = 12.0))
            )
        ))

        runTest {
            tripsRepository.getTrips()

            coVerify {
                flickrApi.getLocationDetails(12.0, 12.0)
            }
        }
    }
})