package com.example.terradownloader


import QueuedViewModel
import android.Manifest
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.terradownloader.Adapter.TabPagerAdapter
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Repository.TeraboxRepository
import com.example.terradownloader.databinding.ActivityMainBinding
import com.example.terradownloader.interfaces.TDInterface
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.Tdutils
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
import java.util.Date


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private var urlId: String = "23456"
    private var pasteUrl: String = ""
    private lateinit var dlink: String

    private lateinit var clipboardManager: ClipboardManager;
    //private var downloadTDDownloadModel: MutableList<TDDownloadModel> = ArrayList()


    private lateinit var mMainActivityMainBinding: ActivityMainBinding
    private lateinit var viewModel: QueuedViewModel
    private var filenameCounter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
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
        val repository = TeraboxRepository(DBTeraboxDatabase.getDatabaseInstance(this))
        val factory = Factory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(QueuedViewModel::class.java)


    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("Permission called", "Permission called request")
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
        //Log.d("Permission called","Permission called")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    displayToastLong(baseContext, "Permission Granted")
                } else {
//                    Log.d(
//                        "Andrroid version",
//                        "Android version is less than " + Build.VERSION.SDK_INT
//                    )
                    //Open Settings to enable permission manually on android 12 and less
                    if (Build.VERSION.SDK_INT < 32)
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
            addToList()

        }

    }

    private fun addToList() {

        //Make a list to update the content in the Queued Fragment

        //downloadTDDownloadModel.add(TDDownloadModel())

        // Increment the filenameCounter
        filenameCounter++

        // Create a new TDDownloadModel with the incremented filename
        val newDownloadModel = TDDownloadModel(
            fileName = "File$filenameCounter",

            // Other properties...
            //other properties

            teraboxFileUrl = "https://www.terabox.com/file/$urlId",
            thumbnailUrl1 = "https://www.terabox.com/file/$urlId",

            thumbnailUrl2 = "https://www.terabox.com/file/$urlId",
            thumbnailUrl3 = "https://www.terabox.com/file/$urlId",

            downloadFileUrl = "3214567bdnfgm",

            fileSize = "0",
            filePath = "downloads",
            downloadStatus = Tdutils.STRING_FETCHING,
            progress = "0",

            isPaused = false,
            downloadStartingDate = Date(),
            downloadFinishingDate = Date(),
            fileUploadDate = 0


        )
        Log.d("File Name ", filenameCounter.toString())

        // Call the insertDownload method in the ViewModel
        viewModel.insertDownload(newDownloadModel)
        //Log.d("File Name ", "File")
        //Iterate the viemodal to the list
        val i = viewModel.getDownloadItems()
        //Iterate the list to log
        for (item in i.value!!) {
            Log.d("File Name ", item.fileName.toString())
        }
    }

    private fun handleDownloadClick(text: String) {
        //Add dummy items to the list upon each click,
        if (checkUrlPatterns(text)) {
            //Log.d("Valid Url regex", "Regex match for terabox");
            //displayToastless(baseContext,"Valid tera box Url");
            //add to the database and to the queued fragment
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
    }
}
