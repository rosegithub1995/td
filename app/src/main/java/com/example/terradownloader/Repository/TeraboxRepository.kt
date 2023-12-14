package com.example.terradownloader.Repository

import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Database.DownloadingTable
import com.example.terradownloader.interfaces.TDInterface
import com.example.terradownloader.utils.Tdutils

class TeraboxRepository(
    private val database: DBTeraboxDatabase
) {

    //get the details ofn the link
    suspend fun fetchLinkDetails(link: String) =
        TDInterface.getTDRetrofitInstance().getTdlink(link)

    //Add the link to the database

    suspend fun addLinkToDatabase(mDownloadingTable: DownloadingTable) =
        database.downloadedDAO()
            .insertCurrentlyDownloadingItemToDatabase(mDownloadingTable)

    fun getAllDatabasePendingFiles() =
        database.downloadedDAO().getCurrentlyDownloadingItemFromDatabase(Tdutils.STRING_COMPLETED)

    // Get all completed Files

    fun getAllCompletedFiles() =
        database.downloadedDAO().getDownloadedItemsFromDatabase(Tdutils.STRING_COMPLETED)

    //Update the file progress using the full cclass instance

    suspend fun updateFileProgress(mDownloadingTable: DownloadingTable) =
        database.downloadedDAO().updateCurrentlyDownloadingItemInDatabase(mDownloadingTable)

    suspend fun deleteFileFromDatabase(mTeraboxFileUrl: String) =
        database.downloadedDAO().deleteByFileUrl(mTeraboxFileUrl)
}
