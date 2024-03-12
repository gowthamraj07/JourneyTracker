package com.gowthamraj07.journeytracker.ui.ongoing.journey

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.gowthamraj07.journeytracker.R
import com.gowthamraj07.journeytracker.domain.Place
import com.gowthamraj07.journeytracker.services.TripsServices
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun OngoingJourneyScreen(
    tripId: Long,
    viewModel: OngoingJourneyViewModel = koinViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState = viewModel.places.collectAsState()

    when (val currentState = uiState.value) {
        is OngoingJourneyUIState.Loading -> {
            CenterLoadingIndicator()
        }

        is OngoingJourneyUIState.Data -> {
            OngoingJourneyScreenContent(navigator, currentState.places)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPlacesFor(tripId)
    }
}

@Composable
fun CenterLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun OngoingJourneyScreenContent(
    navigator: DestinationsNavigator,
    places: Flow<List<Place>>
) {
    Scaffold(
        topBar = { TopBar(navigator) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            val listOfPlace = places.collectAsState(initial = emptyList())
            LazyColumn {
                items(listOfPlace.value) { place ->
                    PlaceDetails(place)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                text = "Ongoing Journey",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        actions = {
            Text(
                text = "Stop",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        val intent = Intent(context, TripsServices::class.java)
                        intent.action = TripsServices.ACTION_STOP_UPDATES
                        context.startService(intent)
                        navigator.navigateUp()
                    }
            )
        },
        modifier = Modifier.shadow(elevation = 4.dp),
    )
}

@Composable
fun PlaceDetails(place: Place) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .heightIn(max = 200.dp)
    ) {
        Box {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = place.imageUrl)
                    .apply(block = {
                        placeholder(R.drawable.bouncing_circles)
                        error(R.drawable.error_loading_image)
                    }).build()
            )

            Image(
                painter = painter,
                contentDescription = "Loaded image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
fun OngoingJourneyPreview() {
    OngoingJourneyScreenContent(
        navigator = EmptyDestinationsNavigator, places = flowOf(
            value = listOf(
                Place(
                    id = 0,
                    tripId = 1,
                    latitude = 12.0,
                    longitude = 13.0,
                    imageUrl = ""
                )
            )
        )
    )
}
