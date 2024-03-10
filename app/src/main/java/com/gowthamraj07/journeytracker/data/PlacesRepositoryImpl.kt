package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PlacesRepositoryImpl(private val placeDao: PlaceDao) : PlacesRepository {
    override suspend fun loadPlacesFor(tripId: Int): Flow<List<Place>> {
        placeDao.getPlacesByTrip(tripId)

        return flowOf(emptyList())
    }
}
