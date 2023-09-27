package com.example.terradownloader

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.util.Log.d
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.net.MalformedURLException


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private lateinit var url: String
    private lateinit var urlId: String
    var pasteUrl: String = ""

    private lateinit var clipboardManager: ClipboardManager;
    private lateinit var item: ClipData.Item;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the views
        textFieldEnterUrl = findViewById(R.id.textField_enter_url)
        downloadButton = findViewById(R.id.downloadButton)
        pasteButton = findViewById(R.id.pasteButton)
    }

    override fun onStart() {
        super.onStart()
        pasteButton.setOnClickListener {
            //Toast.makeText(baseContext, "URL" + pasteUrl, Toast.LENGTH_LONG).show();
            textFieldEnterUrl.setText(pasteUrl);
        }
        // Additional code to be executed when the activity starts
        downloadButton.setOnClickListener {
            if(pasteUrl.isNotBlank())
            handleDownloadClick(pasteUrl)
            else{
                displayToastLong("Not Valid Url");
            }
        }

    }
    fun displayToastless(message:String){
        Toast.makeText(baseContext,message,Toast.LENGTH_SHORT).show();
    }
    fun displayToastLong(message: String){
        Toast.makeText(baseContext,message,Toast.LENGTH_LONG).show();
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        var isAndroid10Plus: Boolean = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q);
        if (!isAndroid10Plus)
            return;
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
        if (!clipboardManager.hasPrimaryClip()) {
            d("Clip Board data", "Not Valid Data");
            return;
        }
        pasteUrl= clipboardManager.primaryClip?.getItemAt(0)?.text as String;
        super.onWindowFocusChanged(hasFocus)
    }

    private fun handleDownloadClick(text: String) {
        if (checkUrlPatterns(text)) {
            //Log.d("Valid Url regex", "Regex match for terabox");
            displayToastless("Valid tera box Url");
            urlId = geturlID(text);

            //make the api call here
        }
        //val link = textFieldEnterUrl.text.toString()
    }


    private fun geturlID(surl: String): String {
        val startIndex = surl.indexOf("s/") + 2 // Add 2 to skip "s/"
        val endIndex = surl.length
        if (startIndex >= 0 && startIndex < endIndex) {
            return surl.substring(startIndex, endIndex)
        } else {
            return "Can not find the Surl";
        }
    }


    fun isValidUrl(url: String): Boolean {
        try {
            URL(url)
            return true
        } catch (e: MalformedURLException) {
            return false
        }
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

//    val epochTimestamp = 1632591600L // Replace with your desired epoch timestamp
//    val formattedDate = convertEpochToDateTime(epochTimestamp)
//    println(formattedDate)


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

