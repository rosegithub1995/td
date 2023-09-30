package com.example.terradownloader.interfaces

import com.example.terradownloader.model.TDDownloadModel

interface ItemClickListener {
    fun onCLickItem(file_path: String?)
    fun onShareClick(downloadModel: TDDownloadModel?)

}