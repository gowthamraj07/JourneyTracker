package com.gowthamraj07.journeytracker.domain.repository

interface LocationRepository {
    fun isServiceNeeded(): Boolean
    suspend fun stopCapturingLocations()
    suspend fun startCapturingLocations()
}