//package com.example.terradownloader.Database
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import java.util.Date
//
//@Entity(tableName = "CurrentlyDownloadingTable")
//data class CurrentlyDownloadingTable(
//    @PrimaryKey(autoGenerate = true) val id: Long, // Id for the CurrentlyDownloadingEntities Table set as PK
//    val idCurrentlyDownloading: Long, // Id from the download manager
//    val teraboxFileUrl: String = "",
//    val thumbnailUrl1: String, // The thumbnail URL for the post
//    val thumbnailUrl2: String, // The thumbnail URL for the post
//    val thumbnailUrl3: String, // The thumbnail URL for the post
//    val downloadFileUrl: String, // The download link of the file it is dlink from the response
//    val fileName: String = "",
//    val fileSize: String = "",
//    val filePath: String = "",
//    val downloadStatus: String = "",
//    val progress: String = "",
//    val isPaused: Boolean = false,
//    val downloadStartingDate: Date,
//    val downloadFinishingDate: Date, // Directly insert like Date() because Converter gonna convert automatically
//    val fileUploadDate: Long // Fetch from the URL response like dlink
//)
