package com.gowthamraj07.journeytracker.data

import app.cash.turbine.test
import com.google.gson.JsonObject
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponse
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
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
    val flickrApi = mockk<FlickrApi>(relaxed = true)
    val flickrResponseParser = mockk<FlickrResponseParser>(relaxed = true)
    val placesRepository = PlacesRepositoryImpl(placeDao, flickrApi, flickrResponseParser, testDispatcher)

    beforeAny{
        Dispatchers.setMain(testDispatcher)
    }

    afterAny {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    "call placeDao to get places" {
        val tripId = 1

        placesRepository.loadPlacesFor(tripId).test {
            cancelAndIgnoreRemainingEvents()
            coVerify {
                placeDao.getPlacesByTrip(tripId)
            }
        }
    }

    "trigger flickr api to get images" {
        val tripId = 1
        coEvery { placeDao.getPlacesByTrip(tripId) } returns flowOf(
            listOf(
                PlaceEntity(
                    id = 1,
                    tripId = 1,
                    latitude = 12.0,
                    longitude = 13.0,
                )
            )
        )

        placesRepository.loadPlacesFor(tripId).test {
            cancelAndIgnoreRemainingEvents()
            coVerify {
                flickrApi.getLocationDetails(12.0, 13.0)
            }
        }
    }

    "pass the flickr json response to FlickrResponseParser" {
        val tripId = 1
        val placeEntity = PlaceEntity(
            id = 1,
            tripId = 1,
            latitude = 12.0,
            longitude = 13.0,
        )
        coEvery { placeDao.getPlacesByTrip(tripId) } returns flowOf(listOf(placeEntity))
        val flickrJsonResponse = JsonObject()
        coEvery { flickrApi.getLocationDetails(placeEntity.latitude, placeEntity.longitude) } returns flickrJsonResponse

        placesRepository.loadPlacesFor(tripId).test {
            cancelAndIgnoreRemainingEvents()
            coVerify {
                flickrResponseParser.parse(flickrJsonResponse)
            }
        }
    }

    "return Places domain from PlaceEntities returned from PlacesDao" {
        val tripId = 1
        val placeEntity = PlaceEntity(
            id = 1,
            tripId = 1,
            latitude = 12.0,
            longitude = 13.0,
        )
        coEvery { placeDao.getPlacesByTrip(tripId) } returns flowOf(listOf(placeEntity))
        coEvery { flickrApi.getLocationDetails(placeEntity.latitude, placeEntity.longitude) } returns flickrJsonResponse
        val flickrResponse = FlickrResponse(
            id = "53573430851",
            serverId = "65535",
            secret = "3005ebe97a"
        )
        coEvery { flickrResponseParser.parse(flickrJsonResponse) } returns flickrResponse

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

private val flickrJsonResponse = """
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