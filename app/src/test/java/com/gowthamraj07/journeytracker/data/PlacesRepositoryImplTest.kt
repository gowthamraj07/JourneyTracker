package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import io.kotest.core.spec.style.StringSpec
import io.mockk.coVerify
import io.mockk.mockk

class PlacesRepositoryImplTest: StringSpec({
    val placeDao = mockk<PlaceDao>(relaxed = true)
    val placesRepository = PlacesRepositoryImpl(placeDao)

    "call placeDao to get places" {
        val tripId = 1

        placesRepository.loadPlacesFor(tripId)

        coVerify {
            placeDao.getPlacesByTrip(tripId)
        }
    }
})