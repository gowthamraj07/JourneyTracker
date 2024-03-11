package com.gowthamraj07.journeytracker.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.gowthamraj07.journeytracker.R
import com.gowthamraj07.journeytracker.data.db.dao.PlaceDao
import com.gowthamraj07.journeytracker.data.db.dao.TripDao
import com.gowthamraj07.journeytracker.data.db.entities.PlaceEntity
import com.gowthamraj07.journeytracker.data.db.entities.TripEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TripsServices : Service() {

    private val tripDao: TripDao by inject()
    private val placeDao: PlaceDao by inject()
    private var tripId: Long = 0

    private lateinit var locationCallback: LocationCallback

    private val channelId = "TripsServiceChannel"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("Track my trip")
            .setContentText("Tracking...")
            .setSmallIcon(R.drawable.ic_notification)
            // Add more notification configuration as needed
            .build()

        startForeground(1, notification)

        CoroutineScope(Dispatchers.IO).launch {
            captureCurrentLocation()
        }

        return START_NOT_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        stopCapturingLocation()
        return super.stopService(name)
    }

    private fun stopCapturingLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private suspend fun captureCurrentLocation() {
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
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private suspend fun save(location: Location) {
        if (tripId == 0L) {
            tripId = tripDao.insert(
                TripEntity(
                    name = "Trip $tripId"
                )
            )
            placeDao.insert(
                PlaceEntity(
                    tripId = tripId,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        } else {
            placeDao.insert(
                PlaceEntity(
                    tripId = tripId,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
        }
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}