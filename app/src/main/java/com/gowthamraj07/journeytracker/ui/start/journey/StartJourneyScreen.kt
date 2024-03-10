package com.gowthamraj07.journeytracker.ui.start.journey

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.gowthamraj07.journeytracker.ui.destinations.OngoingJourneyScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun StartJourneyScreen(
    navigator: DestinationsNavigator
) {

    HandleLocationPermission()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Start Journey",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.shadow(4.dp)
            )
        }
    ) { paddingValues ->
        StartJourneyContent(paddingValues, navigator)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleLocationPermission() {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    when(locationPermissionState.status) {
        is PermissionStatus.Denied -> {
            if((locationPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                ShowDialogToOpenSettingsScreenToEnablePermission()
            } else {
                ShowDialogExplainWhyPermissionIsNeeded(locationPermissionState)
            }
        }
        PermissionStatus.Granted -> {
            // Do nothing
        }
    }

}

@Composable
fun ShowDialogToOpenSettingsScreenToEnablePermission() {
    val context = LocalContext.current

    Dialog(onDismissRequest = { /*Do Nothing*/ }) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Location permission is needed to track your journey",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    // Open settings screen
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Open Settings")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowDialogExplainWhyPermissionIsNeeded(locationPermissionState: PermissionState) {
    Dialog(onDismissRequest = { /*Do Nothing*/ }) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Location permission is needed to track your journey",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(align = Alignment.Center)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    locationPermissionState.launchPermissionRequest()
                }
            ) {
                Text(text = "Request Permission")
            }
        }
    }
}

@Composable
private fun StartJourneyContent(paddingValues: PaddingValues, navigator: DestinationsNavigator) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "You are ready to start your journey!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navigator.navigate(OngoingJourneyScreenDestination(tripId = 0))
            }
        ) {
            Text(text = "Start Journey")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun StartJourneyScreenPreview() {
    StartJourneyScreen(navigator = EmptyDestinationsNavigator)
}