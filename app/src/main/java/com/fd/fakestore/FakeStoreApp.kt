package com.fd.fakestore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FakeStoreApp : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}