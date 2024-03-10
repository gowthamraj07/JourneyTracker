package com.gowthamraj07.journeytracker.domain.repository

import com.gowthamraj07.journeytracker.domain.Place
import kotlinx.coroutines.flow.Flow


interface PlacesRepository {
    suspend fun loadPlacesFor(tripId: Int): Flow<List<Place>>
}
