package com.gowthamraj07.journeytracker.data

import app.cash.turbine.test
import com.google.gson.JsonObject
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
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
    val flickrApi = mockk<FlickrApi> {
        coEvery { getLocationDetails(any(), any()) } returns JsonObject()
    }
    val flickrResponseParser = mockk<FlickrResponseParser> {
        every { parse(any()) } returns FlickrResponse(
            id = "123",
            serverId = "123",
            secret = "123"
        )
    }

    val tripsRepository = TripsRepositoryImpl(tripDao, flickrApi, flickrResponseParser)

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

    "should get the first place of the every trip and trigger flickr search api to get the details of that place" {
        every { tripDao.getTripsWithPlaces() } returns flowOf(
            listOf(
                TripsWithPlaces(
                    TripEntity(id = 1, name = "Trip to beach"),
                    listOf(PlaceEntity(id = 1, tripId = 1, latitude = 12.0, longitude = 12.0))
                )
            )
        )

        tripsRepository.getTrips().test {
            cancelAndIgnoreRemainingEvents()

            coVerify {
                flickrApi.getLocationDetails(12.0, 12.0)
            }
        }
    }

    "should pass the json response received from the flickr api to FlickrResponseParser" {
        // Arrange
        val flickrResponse = JsonObject()
        every { tripDao.getTripsWithPlaces() } returns flowOf(
            listOf(
                TripsWithPlaces(
                    TripEntity(id = 1, name = "Trip to beach"),
                    listOf(PlaceEntity(id = 1, tripId = 1, latitude = 12.0, longitude = 12.0))
                )
            )
        )
        coEvery { flickrApi.getLocationDetails(12.0, 12.0) } returns flickrResponse

        // Act
        tripsRepository.getTrips().test {
            cancelAndIgnoreRemainingEvents()

            // Assert
            coVerify {
                flickrResponseParser.parse(flickrResponse)
            }
        }
    }

    "should return the flow of trips domain from the flow of tripsWithPlaces entities" {
        every { tripDao.getTripsWithPlaces() } returns flowOf(
            listOf(
                TripsWithPlaces(
                    TripEntity(id = 1, name = "Trip to beach"),
                    listOf(PlaceEntity(id = 1, tripId = 1, latitude = 12.0, longitude = 12.0))
                )
            )
        )

        coEvery { flickrApi.getLocationDetails(12.0, 12.0) } returns flickrResponse

        coEvery { flickrResponseParser.parse(flickrResponse) } returns FlickrResponse(
            id = "53573430851",
            serverId = "65535",
            secret = "3005ebe97a"
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

private val flickrResponse = """
            {
                "photos": {
                    "page": 1,
                    "pages": 123861,
                    "perpage": 1,
                    "total": 123861,
                    "photo": [
                        {
                            "id": "53573430851",
                            "owner": "69506664@N06",
                            "secret": "3005ebe97a",
                            "server": "65535",
                            "farm": 66,
                            "title": "2023-11-11_22-45-07_ILCE-7C_DSCBM4637_DxO",
                            "ispublic": 1,
                            "isfriend": 0,
                            "isfamily": 0
                        }
                    ]
                },
                "stat": "ok"
            }
        """.trimIndent().toJsonObject()