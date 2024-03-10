package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlacesRepositoryImpl(
    private val placeDao: PlaceDao,
    private val flickrApi: FlickrApi,
    private val flickrResponseParser: FlickrResponseParser,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PlacesRepository {
    override suspend fun loadPlacesFor(tripId: Int): Flow<List<Place>> {
        placeDao.getPlacesByTrip(tripId).flowOn(ioDispatcher).map { places ->
            places.map { place ->
                val flickrJsonResponse = flickrApi.getLocationDetails(place.latitude, place.longitude)
                val flickrResponse = flickrResponseParser.parse(flickrJsonResponse)
            }
        }.collect{}

        return flowOf(emptyList())
    }
}
