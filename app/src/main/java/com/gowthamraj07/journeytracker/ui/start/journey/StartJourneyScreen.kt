package com.gowthamraj07.journeytracker.ui.start.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gowthamraj07.journeytracker.ui.destinations.OngoingJourneyScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun StartJourneyScreen(
    navigator: DestinationsNavigator
) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Text(
            text = "You are ready to start your journey!",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navigator.navigate(OngoingJourneyScreenDestination)
            }
        ) {
            Text(text = "Start Journey")
        }
    }
}

@Preview
@Composable
fun StartJourneyScreenPreview() {
    StartJourneyScreen(navigator = EmptyDestinationsNavigator)
}