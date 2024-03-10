package com.gowthamraj07.journeytracker.data

import com.google.gson.JsonObject
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
import io.kotest.core.spec.style.StringSpec
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

        placesRepository.loadPlacesFor(tripId)

        coVerify {
            placeDao.getPlacesByTrip(tripId)
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

        placesRepository.loadPlacesFor(tripId)

        coVerify {
            flickrApi.getLocationDetails(12.0, 13.0)
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

        placesRepository.loadPlacesFor(tripId)

        coVerify {
            flickrResponseParser.parse(flickrJsonResponse)
        }
    }
})