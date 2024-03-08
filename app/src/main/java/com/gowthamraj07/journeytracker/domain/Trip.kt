package com.gowthamraj07.journeytracker.domain

import java.net.URL

data class Trip(private val id: Int, val name: String, val image: TripImage)

sealed interface TripImage {
    data class LocalImage(val id: Int) : TripImage
    data class RemoteImage(val url: URL) : TripImage
}
