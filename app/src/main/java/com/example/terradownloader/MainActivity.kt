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
import com.example.terradownloader.utils.Tdutils.checkUrlPatterns
import com.example.terradownloader.utils.Tdutils.displayToastLong
import com.example.terradownloader.utils.Tdutils.displayToastless
import com.example.terradownloader.utils.Tdutils.geturlID
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
            if (pasteUrl.isNotBlank())
                handleDownloadClick(pasteUrl)
            else {
                displayToastLong(baseContext,"Not Valid Url");
            }
        }

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
        pasteUrl = clipboardManager.primaryClip?.getItemAt(0)?.text as String;
        super.onWindowFocusChanged(hasFocus)
    }

    private fun handleDownloadClick(text: String) {
        if (checkUrlPatterns(text)) {
            //Log.d("Valid Url regex", "Regex match for terabox");
            //displayToastless(baseContext,"Valid tera box Url");
            urlId = geturlID(text);

            //make the api call here
        }
        else{
            displayToastless(baseContext,"Invalid Terabox URL");
        }
        //val link = textFieldEnterUrl.text.toString()
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

