package com.example.terradownloader.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.utils.Tdutils

@Dao
interface DownloadDAO {
    companion object {
        const val COMPLETED_STATUS = Tdutils.STRING_COMPLETED// replace with actual completed status
    }
    // Define methods for DownloadingTable
//    @Insert
//    suspend fun insertCurrentlyDownloadingItemToDatabase(currentlyDownloadingTable: DownloadingTable)


    // Define methods for DownloadingTable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentlyDownloadingItemToDatabase(mCurrentlyDownloadingTable: DownloadingTable)


    @Delete
    suspend fun deleteCurrentlyDownloadingItemFromDatabase(mCurrentlyDownloadingTable: DownloadingTable)

    @Query("DELETE FROM DownloadingTable WHERE teraboxFileUrl = :fileUrl")
    suspend fun deleteByFileUrl(fileUrl: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCurrentlyDownloadingItemInDatabase(mCurrentlyDownloadingTable: DownloadingTable)

    @Query("SELECT * FROM DownloadingTable WHERE downloadStatus != :status")
    fun getCurrentlyDownloadingItemFromDatabase(status: String): LiveData<List<TDDownloadModel>>

    @Query("SELECT * FROM DownloadingTable WHERE downloadStatus = :status")
    fun getDownloadedItemsFromDatabase(status:String): LiveData<List<TDDownloadModel>>
}
