package com.gowthamraj07.journeytracker.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
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
import java.text.SimpleDateFormat
import java.util.Locale

class LocationRepositoryImpl(
    private val placeDao: PlaceDao,
    private val tripDao: TripDao,
    private val service: TripsServiceConnection,
    private val flickrApi: FlickrApi,
    private val flickrResponseParser: FlickrResponseParser,
    private val distanceCalculator: DistanceCalculator,
    private val context: Context
) : LocationRepository {

    private var isServiceRunning = false
    private var lastSavedLocation: Location? = null
    private var tripId: Long = 0

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun isServiceNeeded(): Boolean = isServiceRunning

    override suspend fun stopCapturingLocations() {
        isServiceRunning = false
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
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
                        saveIfNewLocationIsMoreThan100MetersFromLastSavedLocation(location)
                    }
                }
            }
        }

        val locationRequest = LocationRequest.Builder(10000).build()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private suspend fun saveIfNewLocationIsMoreThan100MetersFromLastSavedLocation(location: Location) {
        if(lastSavedLocation == null) {
            lastSavedLocation = location
            save(location)
            return
        }

        lastSavedLocation?.let { lastLocation ->

            val distanceOver100Meters = distanceCalculator.isDistanceOver100Meters(
                lastLocation.latitude,
                lastLocation.longitude,
                location.latitude,
                location.longitude
            )

            if (distanceOver100Meters) {
                save(location)
            }
        }

        lastSavedLocation = location
    }

    private suspend fun save(location: Location) {
        if (tripId == 0L) {
            saveTripAndPlace(location)
        } else {
            savePlace(location)
        }
        giveHapticFeedback()
    }

    private fun giveHapticFeedback() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    }

    private suspend fun saveTripAndPlace(location: Location) {
        val dateTime: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(System.currentTimeMillis())
        tripId = tripDao.insert(
            TripEntity(
                name = "Trip on $dateTime"
            )
        )

        service.service?.notifyObserversWithNewTripId(tripId)
        savePlace(location)
    }

    private suspend fun savePlace(location: Location) {
        val flickrJsonResponse = flickrApi.getLocationDetails(location.latitude, location.longitude)
        val flickrResponse = flickrResponseParser.parse(flickrJsonResponse)
        val imageUrl = "https://live.staticflickr.com/${flickrResponse.serverId}/${flickrResponse.id}_${flickrResponse.secret}.jpg"

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