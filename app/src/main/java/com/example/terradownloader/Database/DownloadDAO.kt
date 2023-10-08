package com.example.terradownloader.Database

import TDDownloadModel
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DownloadDAO {
    // Define methods for CurrentlyDownloadingTable
    @Insert
    suspend fun insertCurrentlyDownloadingItemToDatabase(mTDDownloadModel: TDDownloadModel)

    @Delete
    suspend fun deleteCurrentlyDownloadingItemFromDatabase(mTDDownloadModel: TDDownloadModel)

    @Update
    suspend fun updateCurrentlyDownloadingItemInDatabase(mTDDownloadModel: TDDownloadModel)

    @Query("SELECT * FROM CurrentlyDownloadingTable WHERE downloadStatus != 'Completed'")
    fun getCurrentlyDownloadingItemFromDatabase(): LiveData<List<TDDownloadModel>>

    // Define methods for DownloadedTable
    @Insert
    suspend fun insertDownloadedItemToDatabase(mTDDownloadModel: TDDownloadModel)

    @Delete
    suspend fun deleteDownloadedItemFromDatabase(mTDDownloadModel: TDDownloadModel)

    @Update
    suspend fun updateDownloadedItemInDatabase(mTDDownloadModel: TDDownloadModel)

    @Query("SELECT * FROM DownloadedTable")
    fun getDownloadedItemsFromDatabase(): LiveData<List<TDDownloadModel>>
}
