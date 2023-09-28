package com.example.terradownloader.utils

import android.content.Context
import android.webkit.MimeTypeMap
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Tdutils {
    fun geturlID(surl: String): String {
        val startIndex = surl.indexOf("s/") + 2 // Add 2 to skip "s/"
        val endIndex = surl.length
        if (startIndex >= 0 && startIndex < endIndex) {
            return surl.substring(startIndex, endIndex)
        } else {
            return "Can not find the Surl";
        }
    }


    fun getFileMimeType(jsonResponse: String): String {
        var mMimeType=""
        try {


            // Extract the file extension from the server filename
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(jsonResponse)

            // Get the MIME type based on the file extension
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
            if (mimeType != null) {
                mMimeType=mimeType
            };
            return mMimeType
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return mMimeType; // Return null if there was an error or the MIME type couldn't be determined
    }
    fun getFileName(jsonResponse: String): String {
        var mFileName=jsonResponse
        try {
            return mFileName
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return mFileName;
    }

    fun isValidUrl(url: String): Boolean {
        try {
            URL(url)
            return true
        } catch (e: MalformedURLException) {
            return false
        }
    }
    fun displayToastless(baseContext: Context, message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show();
    }

    fun displayToastLong(baseContext: Context,message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show();
    }

    fun checkUrlPatterns(url: String): Boolean {
        val patterns = listOf(
            """ww\.mirrobox\.com""".toRegex(),
            """www\.nephobox\.com""".toRegex(),
            """freeterabox\.com""".toRegex(),
            """www\.freeterabox\.com""".toRegex(),
            """1024tera\.com""".toRegex(),
            """4funbox\.co""".toRegex(),
            """www\.4funbox\.com""".toRegex(),
            """mirrobox\.com""".toRegex(),
            """nephobox\.com""".toRegex(),
            """terabox\.app""".toRegex(),
            """terabox\.com""".toRegex(),
            """www\.terabox\.ap""".toRegex(),
            """terabox\.fun""".toRegex(),
            """www\.terabox\.com""".toRegex(),
            """www\.1024tera\.co""".toRegex(),
            """www\.momerybox\.com""".toRegex(),
            """teraboxapp\.com""".toRegex(),
            """momerybox\.com""".toRegex(),
            """tibibox\.com""".toRegex(),
            """www\.tibibox\.com""".toRegex(),
            """www\.teraboxapp\.com""".toRegex()
        )

        if (!isValidUrl(url)) {
            return false
        }

        for (pattern in patterns) {
            if (pattern.containsMatchIn(url)) {
                return true
            }
        }

        return false
    }


    fun getFormattedSize(sizeBytes: Double): String {
        val size: Double
        val unit: String

        when {
            sizeBytes >= 1024 * 1024 -> {
                size = sizeBytes.toDouble() / (1024 * 1024)
                unit = "MB"
            }

            sizeBytes >= 1024 -> {
                size = sizeBytes.toDouble() / 1024
                unit = "KB"
            }

            else -> {
                size = sizeBytes.toDouble()
                unit = "bytes"
            }
        }

        return "${"%.2f".format(size)} $unit"
    }
//    val sizeBytes = 1500.0 // Replace with your desired size in bytes as a Double
//    val formattedSize = getFormattedSize(sizeBytes)
//    println(formattedSize)

    fun convertEpochToDateTime(epochTimestamp: Long): String {
        val normalDate = Date(epochTimestamp * 1000)

        val dateFormat = SimpleDateFormat("yyyy MMMM dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(normalDate)
    }
}