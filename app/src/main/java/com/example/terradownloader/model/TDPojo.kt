package com.example.terradownloader.model

data class TDPojo(
    val category: String,
    val fs_id: String,
    val isdir: String,
    val local_ctime: String,
    val local_mtime: String,
    val path: String,
    val play_forbid: String,
    val server_ctime: String,
    val server_filename: String,
    val server_mtime: String,
    val size: String,
    val dlink: String,
    val thumbs: Thumbs,
    val emd5: String
)

data class Thumbs(
    val url1: String,
    val url2: String,
    val url3: String,
    val icon: String
)
