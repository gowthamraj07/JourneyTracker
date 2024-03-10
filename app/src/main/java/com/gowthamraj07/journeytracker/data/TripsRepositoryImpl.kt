package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripsRepositoryImpl(
    private val tripDao: TripDao,
    private val flickrApi: FlickrApi,
    private val flickrResponseParser: FlickrResponseParser
) : TripsRepository {
    override suspend fun getTrips(): Flow<List<Trip>> {
        return tripDao.getTripsWithPlaces().map {
            it.map { tripEntity ->
                val place = tripEntity.places.first()
                val flickrJsonResponse =
                    flickrApi.getLocationDetails(place.latitude, place.longitude)
                val flickrResponse = flickrResponseParser.parse(flickrJsonResponse)
                Trip(
                    id = tripEntity.trip.id,
                    name = tripEntity.trip.name,
                    image = TripImage.RemoteImage(
                        "https://live.staticflickr.com/${flickrResponse.serverId}/${flickrResponse.id}_${flickrResponse.secret}.jpg"
                    )
                )
            }
        }
    }
}
