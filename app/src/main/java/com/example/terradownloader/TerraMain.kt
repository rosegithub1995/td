package com.example.terradownloader

import android.app.Application
import com.google.android.material.color.DynamicColors


class TerraMain : Application() {
    //lateinit var repository: TeraboxRepository
    //lateinit var mApplicationRoomDatabaseInstance: DBTeraboxDatabase
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
//        mApplicationRoomDatabaseInstance = DBTeraboxDatabase.getDatabaseInstance(this);
//        repository = TeraboxRepository(
//            mApplicationRetrofitTdInstance,
//            this,
//            mApplicationRoomDatabaseInstance
//        )
    }
}
