package com.gowthamraj07.journeytracker.domain

data class Place(
    val id: Long,
    val tripId: Long,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)