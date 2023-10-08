package com.example.terradownloader.interfaces

import TDDownloadModel


interface ItemClickListener {
    fun onCLickItem(file_path: String?)
    fun onShareClick(downloadModel: TDDownloadModel?)

}
