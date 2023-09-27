package com.example.terradownloader

import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the views
        textFieldEnterUrl = findViewById(R.id.textField_enter_url)
        downloadButton = findViewById(R.id.downloadButton)
        pasteButton = findViewById(R.id.pasteButton)

        // Now you can work with these views as needed
        // For example, you can set click listeners for the buttons, etc.
    }

    override fun onStart() {
        super.onStart()
        // Additional code to be executed when the activity starts
        downloadButton.setOnClickListener{
            handleDownloadClick()
        }

    }
    private fun handleDownloadClick() {
        // Get the user-entered link from linkEditText
        val link = textFieldEnterUrl.text.toString()
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

//    val epochTimestamp = 1632591600L // Replace with your desired epoch timestamp
//    val formattedDate = convertEpochToDateTime(epochTimestamp)
//    println(formattedDate)




    fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: Exception) {
            false
        }
    }

//    fun main() {
//        val inputUrl = "https://www.example.com" // Replace with the URL you want to check
//        val matchesPattern = checkUrlPatterns(inputUrl)
//        if (matchesPattern) {
//            println("$inputUrl matches one of the patterns.")
//        } else {
//            println("$inputUrl does not match any of the patterns.")
//        }
//    }
}

