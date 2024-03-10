package com.gowthamraj07.journeytracker.domain

data class Place(
    val id: Int,
    val tripId: Int,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)