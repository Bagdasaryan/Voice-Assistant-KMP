package com.mb.voiceassistantkmp

import android.app.Application
import com.mb.voiceassistantkmp.di.commonModule
import com.mb.voiceassistantkmp.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class AssistantApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@AssistantApp)
            modules(commonModule(), platformModule())
        }
    }
}