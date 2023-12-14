package com.example.terradownloader

import QueuedViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.terradownloader.Repository.TeraboxRepository

class Factory(private val app: Application, private val repository: TeraboxRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QueuedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QueuedViewModel(app, repository) as T
        }
        throw IllegalArgumentException("Unable to construct ViewModel")
    }
}
