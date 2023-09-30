package com.example.terradownloader.model

class TDDownloadModel {
    // For displaying in Recycler View

    var mDownloadId: Long = 0
    var mId: Long = 0 // for custom download id
    var mFileName: String = ""
    var mFileSize: String = ""
    var mFilePath: String = ""
    var mStatus: String = ""
    var mProgress: String = ""
    var mIsPaused: Boolean = false

    fun setmIsPaused(paused: Boolean) {
        this.mIsPaused = paused
    }

    fun setmStatus(status: String) {
        this.mStatus = status
    }

    fun setmFilePath(filePath: String) {
        this.mFilePath = filePath
    }

    fun setmFileName(fileName: String) {
        this.mFileName = fileName
    }

    fun setmFileSize(fileSize: String) {
        this.mFileSize = fileSize
    }

    fun setmProgress(progress: String) {
        this.mProgress = progress
    }

    fun setmDownloadId(downloadId: Long) {
        this.mDownloadId = downloadId
    }

    fun setmId(id: Long) {
        this.mId = id
    }

    fun getmDownloadId(): Long {
        return mDownloadId
    }

    fun getmId(): Long {
        return mId
    }

    fun getmFileName(): String {
        return mFileName
    }

    fun getmFileSize(): String {
        return mFileSize
    }

    fun getmFilePath(): String {
        return mFilePath
    }

    fun getmStatus(): String {
        return mStatus
    }

    fun getmProgress(): String {
        return mProgress
    }

    fun getmIsPaused(): Boolean {
        return mIsPaused
    }
}
