package com.example.terradownloader.model

class TDDownloadModel {
    //For displaying in Recycler View

    var downloadId: Long = 0;
    var id: Long = 0;// for custom download id
    var fileName: String = "";
    var fileSize: Long = 0;
    var filePath: String = "";
    var status: String = "";
    var progress: String = "";
    var isPaused: Boolean = false;

    fun setIsPaused(paused: Boolean) {
        this.isPaused = paused
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun setFilePath(filePath: String) {
        this.filePath = filePath
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    fun setFileSize(fileSize: Long) {
        this.fileSize = fileSize
    }

    fun setProgress(progress: String) {
        this.progress = progress
    }

    fun setDownloadId(downloadId: Long) {
        this.downloadId = downloadId
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getDownloadId(): Long {
        return downloadId
    }

    fun getId(): Long {
        return id
    }

    fun getFileName(): String {
        return fileName
    }

    fun getFileSize(): Long {
        return fileSize
    }

    fun getFilePath(): String {
        return filePath
    }

    fun getStatus(): String {
        return status
    }

    fun getProgress(): String {
        return progress
    }

    fun getIsPaused(): Boolean {
        return isPaused
    }

}