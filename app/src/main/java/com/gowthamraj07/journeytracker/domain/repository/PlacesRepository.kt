package com.gowthamraj07.journeytracker.domain.repository

interface PlacesRepository {
    suspend fun loadPlaces()
}
