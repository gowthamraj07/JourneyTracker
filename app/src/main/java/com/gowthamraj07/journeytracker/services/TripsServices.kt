package com.gowthamraj07.journeytracker.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.gowthamraj07.journeytracker.R
import com.gowthamraj07.journeytracker.domain.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject

class TripsServices : LifecycleService() {
    private val locationRepository: LocationRepository by inject()
    private lateinit var locationCallback: LocationCallback

    private val tripServiceBinder = TripServiceBinder()

    private val _isRunning = MutableStateFlow(false)
    var isRunning: Flow<Boolean> = _isRunning
    private val _tripId = MutableStateFlow<Long>(0)
    val tripId: Flow<Long> = _tripId

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return tripServiceBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        lifecycleScope.launch {
            delay(MIN_DELAY_TO_UNBIND)
        }
        return true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.action == ACTION_STOP_UPDATES) {
            _isRunning.update { false }
            stopLocationUpdates()
            stopSelf()
        } else {
            _isRunning.update { true }
            startShowingNotifications()

            lifecycleScope.launch {
                locationRepository.startCapturingLocations()
            }
        }

        return START_STICKY
    }

    private fun startShowingNotifications() {
        // Create the NotificationChannel
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Trips Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)

        // Create the notification
        val pendingIntent = getPendingIntentToOpenTheAppOnClickingNotification()
        val stopIntent = getPendingIntentToStopTheService()
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Track my trip")
            .setContentText("Tracking your trip")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .addAction(R.drawable.ic_delete, "stop", stopIntent)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .build()


        if (hasNecessaryPermissionsAvailable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        }
    }

    private fun hasNecessaryPermissionsAvailable() = (
            ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            )

    private fun getPendingIntentToStopTheService(): PendingIntent? = PendingIntent.getService(
        this,
        0,
        Intent(this, this::class.java).setAction(ACTION_STOP_UPDATES),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun getPendingIntentToOpenTheAppOnClickingNotification(): PendingIntent? =
        PendingIntent.getActivity(
            this,
            0,
            packageManager.getLaunchIntentForPackage(this.packageName),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    private fun stopLocationUpdates() {
        lifecycleScope.launch {
            locationRepository.stopCapturingLocations()
        }
    }

    override fun stopService(name: Intent?): Boolean {
        stopCapturingLocation()
        return super.stopService(name)
    }

    private fun stopCapturingLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun notifyObserversWithNewTripId(tripId: Long) {
        _tripId.update {
            tripId
        }
    }

    internal inner class TripServiceBinder : Binder() {
        fun getService(): TripsServices {
            return this@TripsServices
        }
    }

    companion object {
        const val MIN_DELAY_TO_UNBIND = 2000L
        const val ACTION_STOP_UPDATES = BuildConfig.LIBRARY_PACKAGE_NAME + "STOP_SERVICE"
        const val NOTIFICATION_CHANNEL_ID = "LocationUpdates"
        const val NOTIFICATION_ID = 1
    }
}

class TripsServiceConnection : ServiceConnection {

    var service: TripsServices? = null
        private set

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        service = (binder as TripsServices.TripServiceBinder).getService()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }

}


