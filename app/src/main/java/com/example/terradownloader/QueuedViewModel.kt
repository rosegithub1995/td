package com.example.terradownloader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.terradownloader.model.TDDownloadModel

class QueuedViewModel(application: Application) : AndroidViewModel(application) {
    fun getDownloadItems(): List<TDDownloadModel> {
        TODO("Not yet implemented")
    }
    // TODO: Implement the ViewModel
}
