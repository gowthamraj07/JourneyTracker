package com.gowthamraj07.journeytracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gowthamraj07.journeytracker.services.TripsServiceConnection
import com.gowthamraj07.journeytracker.services.TripsServices
import com.gowthamraj07.journeytracker.ui.NavGraphs
import com.gowthamraj07.journeytracker.ui.theme.JourneyTrackerTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val tripsServiceConnection: TripsServiceConnection by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, TripsServices::class.java)
        bindService(serviceIntent, tripsServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(tripsServiceConnection)
    }
}