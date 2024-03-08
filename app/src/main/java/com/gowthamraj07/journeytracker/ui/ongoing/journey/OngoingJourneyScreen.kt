package com.gowthamraj07.journeytracker.ui.ongoing.journey

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gowthamraj07.journeytracker.domain.Place
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun OngoingJourneyScreen(
    places: Flow<List<Place>> = koinViewModel<OngoingJourneyViewModel>().places,
    navigator: DestinationsNavigator
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
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
                        modifier = Modifier.padding(16.dp).clickable {
                            navigator.navigateUp()
                        }
                    )
                },
                modifier = Modifier.shadow(elevation = 4.dp),
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn {
                coroutineScope.launch {
                    places.collectLatest {
                        items(it) { place ->
                            PlaceDetails(place)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceDetails(place: Place) {
    Card(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        AsyncImage(model = place.imageUrl, contentDescription = null)
    }
}

@Preview
@Composable
fun OngoingJourneyPreview() {
    OngoingJourneyScreen(flowOf(emptyList()), navigator = EmptyDestinationsNavigator)
}
