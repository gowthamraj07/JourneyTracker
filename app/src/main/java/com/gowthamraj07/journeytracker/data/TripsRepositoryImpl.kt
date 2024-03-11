package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.gowthamraj07.journeytracker.domain.repository.TripsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TripsRepositoryImpl(
    private val tripDao: TripDao
) : TripsRepository {
    override suspend fun getTrips(): Flow<List<Trip>> {
        return tripDao.getTripsWithPlaces().map {
            it.filter { it.places.isNotEmpty() }.map { tripEntity ->
                Trip(
                    id = tripEntity.trip.id,
                    name = tripEntity.trip.name,
                    image = TripImage.RemoteImage(
                        tripEntity.places.first().imageUrl,
                    )
                )
            }
        }
    }
}
