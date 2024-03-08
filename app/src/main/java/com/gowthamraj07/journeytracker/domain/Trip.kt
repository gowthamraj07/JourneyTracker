package com.gowthamraj07.journeytracker.domain

data class Trip(private val id: Int, val name: String, val image: TripImage)

sealed interface TripImage {
    data class LocalImage(val id: Int) : TripImage
    data class RemoteImage(val url: String) : TripImage
}
