package com.gowthamraj07.journeytracker.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripEntity
import com.gowthamraj07.journeytracker.data.flikr.FlickrApi
import com.gowthamraj07.journeytracker.data.flikr.FlickrResponseParser
import com.gowthamraj07.journeytracker.domain.repository.LocationRepository
import com.gowthamraj07.journeytracker.services.TripsServiceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationRepositoryImpl(
    private val placeDao: PlaceDao,
    private val tripDao: TripDao,
    private val service: TripsServiceConnection,
    private val flickrApi: FlickrApi,
    private val flickrResponseParser: FlickrResponseParser,
    private val context: Context
): LocationRepository {

    private var isServiceRunning = false
    private var tripId: Long = 0

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun isServiceNeeded(): Boolean = isServiceRunning

    override suspend fun stopCapturingLocations() {
        isServiceRunning = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override suspend fun startCapturingLocations() {
        isServiceRunning = true
        captureCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun captureCurrentLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    CoroutineScope(Dispatchers.IO).launch {
                        save(location)
                    }
                }
            }
        }

        val locationRequest = LocationRequest.Builder(10000).build()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private suspend fun save(location: Location) {
        if (tripId == 0L) {
            saveTripAndPlace(location)
        } else {
            savePlace(location)
        }
    }

    private suspend fun saveTripAndPlace(location: Location) {
        tripId = tripDao.insert(
            TripEntity(
                name = "Trip $tripId"
            )
        )

        Log.d("Gowtham", "startCapturingLocations: notifyObserversWithNewTripId with trip id ($tripId)")
        service.service?.notifyObserversWithNewTripId(tripId)

        Log.d("Gowtham", "saveTripAndPlace: trip with new id $tripId")
        savePlace(location)
    }

    private suspend fun savePlace(location: Location) {
        Log.d("Gowtham", "savePlace: saving new place")

        val flickrJsonResponse =
            flickrApi.getLocationDetails(location.latitude, location.longitude)
        val flickrResponse = flickrResponseParser.parse(flickrJsonResponse)
        val imageUrl =
            "https://live.staticflickr.com/${flickrResponse.serverId}/${flickrResponse.id}_${flickrResponse.secret}.jpg"


        placeDao.insert(
            PlaceEntity(
                tripId = tripId,
                latitude = location.latitude,
                longitude = location.longitude,
                imageUrl = imageUrl
            )
        )
    }
}