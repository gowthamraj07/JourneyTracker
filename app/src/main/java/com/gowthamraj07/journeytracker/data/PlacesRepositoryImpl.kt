package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlacesRepositoryImpl(
    private val placeDao: PlaceDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlacesRepository {
    override suspend fun loadPlacesFor(tripId: Long): Flow<List<Place>> {
        return placeDao.getPlacesByTrip(tripId).flowOn(ioDispatcher).map { places ->
            places.map { place ->
                Place(
                    id = place.id,
                    tripId = place.tripId,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    imageUrl = place.imageUrl
                )
            }
        }
    }
}
