package com.example.terradownloader.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.d

class DownloadCompleteReceiver: BroadcastReceiver() {
    private lateinit var mDownloadManager: DownloadManager
    override fun onReceive(context: Context?, intent: Intent?) {
        mDownloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager;
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1L);
            if(id !=-1L){
                val query= id?.let { DownloadManager.Query().setFilterById(it) };
                d("Download ID finished", id.toString());
            }
            context?.sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"))
        }
    }
}