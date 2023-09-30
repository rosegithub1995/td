package com.example.terradownloader.interfaces

import java.io.File

interface Downloader {

    fun downloadFile(url: String, response: String, file: File): Long
}