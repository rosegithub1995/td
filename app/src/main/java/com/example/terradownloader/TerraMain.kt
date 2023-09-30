package com.example.terradownloader

import android.app.Application
import com.google.android.material.color.DynamicColors
import io.realm.gradle.Realm

class TerraMain: Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}