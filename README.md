# Journey Tracker
----------------
This is a simple android application that allows users to track their journeys. It takes periodic photos from Flickr using the user's current location and stores them in a local database.

## Technologies
- Android
- Kotlin
- Jetpack compose
- Koin
- Coroutines
- Raamcosta library for Compose Navigation (Github link)[https://github.com/raamcosta/compose-destinations]
- Room
- Retrofit
- Flickr API
- Google Location Services
- Kotest for Unit testing
- Shot for UI testing

## Architecture
 - Single activity architecture with Jetpack Compose screens
 - Navigation between screens using Compose Destinations library
 - Koin for dependency injection
 - Retrofit for network requests
 - Room for local database
 - Coroutines for asynchronous operations
 - Kotest and Mockk for testing
 - Shot for UI testing
 - LifecycleService for tracking location updates via LocationRepository

## Features
- User can view a list of journeys
- User can start a journey
- User can stop a journey

## Setup to run Unit testing inside Android Studio
- Install the Kotest plugin from the Android Studio plugin marketplace
- URL: https://plugins.jetbrains.com/plugin/14080-kotest

## Setup to run screenshot testing
- Use the following gradle command to run the screenshot tests and to capture the screenshots
```bash
./gradlew :app:executeScreenshotTests -Precord
```
- Use the following gradle command to run the screenshot tests and to compare the screenshots
```bash
./gradlew :app:executeScreenshotTests
```

## Note
- The main focus is on the architecture and the use of Jetpack Compose. 
  - The error handling and edge cases are handled with limited scope
  - The Flickr api does not provide places only photos. As it is randomly returning images for the given location for the same location, the images are not guaranteed to be relevant to the location. 


