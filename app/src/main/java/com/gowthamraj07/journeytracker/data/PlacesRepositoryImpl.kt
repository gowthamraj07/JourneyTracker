package com.gowthamraj07.journeytracker.data

import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow

class PlacesRepositoryImpl : PlacesRepository {
    override suspend fun loadPlacesFor(tripId: Int): Flow<List<Place>> {
        TODO("Not yet implemented")
    }
}
