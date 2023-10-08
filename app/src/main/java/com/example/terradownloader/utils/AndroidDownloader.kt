package com.example.terradownloader.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log.d
import androidx.core.net.toUri
import com.example.terradownloader.interfaces.Downloader
import com.example.terradownloader.utils.Tdutils.getFileMimeType
import com.example.terradownloader.utils.Tdutils.getFileName
import java.io.File

class AndroidDownloader(private val context: Context) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java);

    private lateinit var downloadRequest: DownloadManager.Request;
    private lateinit var fileMimeType: String;
    private var fileName: String = "download";
    override fun downloadFile(url: String, response: String, file: File): Long {
        downloadRequest = DownloadManager.Request(url.toUri());
        fileMimeType = getFileMimeType(response);
        fileName = getFileName(response);
        d("Downloading", "Started download");
        downloadRequest.setMimeType(fileMimeType)
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadRequest.setDestinationUri(Uri.fromFile(file))
        downloadRequest.setTitle(fileName);
        downloadRequest.setDescription("Downloading");
        return downloadManager.enqueue(downloadRequest);

    }
}
