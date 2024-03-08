package com.gowthamraj07.journeytracker.ui.trips

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.gowthamraj07.journeytracker.R
import com.gowthamraj07.journeytracker.domain.Trip
import com.gowthamraj07.journeytracker.domain.TripImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@RootNavGraph(start = true)
@Destination
@Composable
fun TripsScreen(
    uiStateFlow: StateFlow<TripsUiState> = koinViewModel<TripsViewModel>().state,
    navigator: DestinationsNavigator,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Trip")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val uiState = uiStateFlow.collectAsState()
            when (uiState.value) {
                is TripsUiState.Empty -> EmptyTripsState()
                is TripsUiState.Data -> ListOfTripsState((uiState.value as TripsUiState.Data).trips)
            }
        }
    }
}

@Composable
fun ListOfTripsState(trips: Flow<List<Trip>>) {
    val tripsList = trips.collectAsState(initial = emptyList())

    Column {
        tripsList.value.forEach { trip ->
            TripDetails(trip)
        }
    }
}

@Composable
private fun TripDetails(trip: Trip) {
    Card(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .heightIn(max = 200.dp)
    ) {
        Box {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = trip.image)
                    .apply<ImageRequest.Builder>(block = {
                        placeholder(R.drawable.bouncing_circles)
                        error(R.drawable.error_loading_image)
                    }).build()
            )

            Image(
                painter = painter,
                contentDescription = "Loaded image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = trip.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Center)
                        .padding(16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun ColumnScope.EmptyTripsState() {
    Box(modifier = Modifier.weight(1f), contentAlignment = Center) {
        Text(
            text = "You don't have any trips yet. Feel free to add one!",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun TripsPreview_EmptyState() {
    TripsScreen(
        uiStateFlow = MutableStateFlow(TripsUiState.Empty),
        navigator = EmptyDestinationsNavigator
    )
}

@Preview
@Composable
fun TripsPreview_DataState() {
    TripsScreen(
        uiStateFlow = MutableStateFlow(
            TripsUiState.Data(
                flowOf(
                    listOf(
                        Trip(
                            id = 1,
                            name = "Trip to the beach",
                            image = TripImage.LocalImage(R.drawable.bouncing_circles)
                        ),
                        Trip(
                            id = 2,
                            name = "Trip to the mountains",
                            image = TripImage.LocalImage(R.drawable.error_loading_image)
                        ),
                    )
                )
            )
        ),
        navigator = EmptyDestinationsNavigator
    )
}