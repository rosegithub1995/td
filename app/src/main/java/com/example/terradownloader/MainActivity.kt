package com.example.terradownloader


import android.Manifest
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.terradownloader.Adapter.TabPagerAdapter
import com.example.terradownloader.databinding.ActivityMainBinding
import com.example.terradownloader.interfaces.TDInterface
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.Tdutils.checkUrlPatterns
import com.example.terradownloader.utils.Tdutils.displayToastLong
import com.example.terradownloader.utils.Tdutils.displayToastless
import com.example.terradownloader.utils.Tdutils.geturlID
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private lateinit var urlId: String
    private var pasteUrl: String = ""
    private lateinit var dlink: String

    private lateinit var clipboardManager: ClipboardManager;
    //private var downloadTDDownloadModel: MutableList<TDDownloadModel> = ArrayList()


    private lateinit var mMainActivityMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMainActivityMainBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mMainActivityMainBinding.root);
        val viewPager: ViewPager2 = mMainActivityMainBinding.viewPager
        setupViewPager(viewPager)

        val tabLayout = mMainActivityMainBinding.tabLayout
        // Assuming you are using TabLayoutMediator for tab setup
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Queued"
                1 -> tab.text = "Downloaded"
            }
        }.attach()


        //Initlize the DB
        //To insert the data into DB that has suspend function you have to call using


        //Initialize the views
        textFieldEnterUrl = mMainActivityMainBinding.textFieldEnterUrl;
        downloadButton = mMainActivityMainBinding.downloadButton
        pasteButton = mMainActivityMainBinding.pasteButton;

        //Function that request permission for storage
        requestStoragePermission()

    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                displayStoragePermissionDialog()
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
                )

                // REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private fun displayStoragePermissionDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Storage Permission Required")
        builder.setMessage("This app need storage permission to download files")
        builder.setPositiveButton("Allow") { dialog, which ->
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION
            )
        }
        builder.setNegativeButton("Deny") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    displayToastLong(baseContext, "Permission Granted")
                } else {
                    //Open Settings to enable permission manually
                    Snackbar.make(
                        mMainActivityMainBinding.root,
                        "Enable Storage Permission",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Settings") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }.show()
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter = TabPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        pasteButton.setOnClickListener {
            //Toast.makeText(baseContext, "URL" + pasteUrl, Toast.LENGTH_LONG).show();
            try {

                clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                if (!clipboardManager.hasPrimaryClip()) {
                    //  d("Clip Board data", "Not Valid Data");
                    if (!clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)!!) displayToastless(
                        baseContext, "Not Valid Data"
                    );
                    textFieldEnterUrl.setText("Not Valid Data");
                } else {
                    if (clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)!!) {
                        pasteUrl = clipboardManager.primaryClip?.getItemAt(0)?.text as String;
                        textFieldEnterUrl.setText(pasteUrl);
                    }
                }
            } catch (e: Exception) {
                displayToastless(baseContext, "Can not get Copied Item");
            }
        }
        // Additional code to be executed when the activity starts
        downloadButton.setOnClickListener {
            if (pasteUrl.isEmpty()) displayToastless(baseContext, "Url is required");
            else {
                handleDownloadClick(pasteUrl)
                //displayToastLong(baseContext, "Not Valid Url");
            }
        }

    }

    private fun handleDownloadClick(text: String) {
        if (checkUrlPatterns(text)) {
            //Log.d("Valid Url regex", "Regex match for terabox");
            //displayToastless(baseContext,"Valid tera box Url");
            urlId = geturlID(text);
            //d("S param from terabox", urlId);

            GlobalScope.launch {
                val dlinkFetchResponse = TDInterface.getTDRetrofitInstance().getTdlink(urlId);
                //d("url calld", dlink.toString());
                dlinkFetchResponse.enqueue(object : Callback<TDPojo> {
                    override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()!!
                            dlink = responseBody.dlink.toString();
                            //mFileName = responseBody.server_filename.toString();
                            //mFileSize = responseBody.size.toString();
                            //d("File name", mFileName);
                            //startDownloadingFile(dlink);
                            //d("Response data", responseBody.toString());
                            //d("d link", dlink);
                        }
                    }

                    override fun onFailure(call: Call<TDPojo>, t: Throwable) {
                        //d("Error in retrofit call", "Retrofit call error ", t);
                        displayToastLong(baseContext, "Could not fetch details");

                    }
                })
                //make the api call here
            }

        } else {
            displayToastless(baseContext, "Invalid Terabox URL");
        }


        //val link = textFieldEnterUrl.text.toString()
    }


//    private fun updateDownloadStatus(downloadModel: TDDownloadModel) {
//        val index =
//            downloadTDDownloadModel.indexOfFirst { it.mDownloadId == downloadModel.mDownloadId }
//        if (index != -1) {
//            downloadTDDownloadModel[index] = downloadModel
//            runOnUiThread {
//                mTDAdapter.notifyItemChanged(index)
//            }
//        }
//    }


    override fun onDestroy() {
        //Add the difference of the data back to the DB to both the Tables
        super.onDestroy()
    }


//    val epochTimestamp = 1632591600L // Replace with your desired epoch timestamp
//    val formattedDate = convertEpochToDateTime(epochTimestamp)
//    println(formattedDate)

}
