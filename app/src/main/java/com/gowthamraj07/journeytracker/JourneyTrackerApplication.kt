package com.gowthamraj07.journeytracker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin

class JourneyTrackerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@JourneyTrackerApplication)
            modules(dependencies)
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}