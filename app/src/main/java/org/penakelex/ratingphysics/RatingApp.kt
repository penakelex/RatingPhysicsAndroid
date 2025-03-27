package org.penakelex.ratingphysics

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.penakelex.ratingphysics.di.appModule

class RatingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RatingApp)
            modules(appModule)
        }
    }
}

