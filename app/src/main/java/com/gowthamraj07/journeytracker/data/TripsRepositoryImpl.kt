package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TripsRepositoryImpl(
    private val tripDao: TripDao,
    private val flickrApi: FlickrApi,
    private val flickrResponseParser: FlickrResponseParser
) : TripsRepository {
    override suspend fun getTrips(): Flow<List<Trip>> {
        tripDao.getTripsWithPlaces().collect {
            it.map { tripEntity ->
                val place = tripEntity.places.first()
                val flickrResponse = flickrApi.getLocationDetails(place.latitude, place.longitude)
                flickrResponseParser.parse(flickrResponse)
            }
        }

        return flowOf(emptyList())
    }
}
