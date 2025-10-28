package com.gymnastic.ecommerceapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EcomApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
