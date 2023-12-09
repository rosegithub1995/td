package com.example.terradownloader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.terradownloader.Database.CurrentlyDownloadingTable
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Database.DownloadDAO
import com.example.terradownloader.model.TDDownloadModel

class QueuedViewModel(application: Application) : AndroidViewModel(application) {
    private val downloadDao: DownloadDAO = DBTeraboxDatabase.getDatabaseInstance(application).downloadedDAO()

    fun getDownloadItems(): LiveData<List<DownloadEntity>> {
        return downloadDao.getQueuedDownloads().asLiveData()
    }

    suspend fun insertDownload(download: CurrentlyDownloadingTable) {
        downloadDao.insertCurrentlyDownloadingItemToDatabase(download)
    }
}
