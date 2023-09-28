package com.example.terradownloader.interfaces

interface Downloader {

    fun downloadFile(url:String, response:String):Long
}