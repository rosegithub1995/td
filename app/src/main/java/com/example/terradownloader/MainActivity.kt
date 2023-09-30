package com.example.terradownloader

import DownloadStatusUtil
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.Adapter.TDAdapter
import com.example.terradownloader.interfaces.ItemClickListener
import com.example.terradownloader.interfaces.TDService
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.AndroidDownloader
import com.example.terradownloader.utils.Tdutils.checkUrlPatterns
import com.example.terradownloader.utils.Tdutils.displayToastLong
import com.example.terradownloader.utils.Tdutils.displayToastless
import com.example.terradownloader.utils.Tdutils.geturlID
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class MainActivity : AppCompatActivity(), ItemClickListener {
    companion object {
        private const val REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    }

    private lateinit var textFieldEnterUrl: TextInputEditText
    private lateinit var downloadButton: Button
    private lateinit var pasteButton: Button
    private lateinit var mRecyclerView: RecyclerView;
    private lateinit var urlId: String
    var pasteUrl: String = ""

    private lateinit var clipboardManager: ClipboardManager;
    private lateinit var item: ClipData.Item;

    private lateinit var mDownloader: AndroidDownloader
    private var downloadTDDownloadModel: MutableList<TDDownloadModel> = ArrayList()
    private lateinit var mTDAdapter: TDAdapter;
    private var mFileName: String = "dummyFileName";
    private var mFileSize: String = "1 MB";
    private var mDownloadPath: String = "downloads";


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the views
        textFieldEnterUrl = findViewById(R.id.textField_enter_url)
        downloadButton = findViewById(R.id.downloadButton)
        pasteButton = findViewById(R.id.pasteButton)
        mRecyclerView = findViewById(R.id.recycler_view);


        mTDAdapter = TDAdapter(this@MainActivity, downloadTDDownloadModel, this@MainActivity)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this@MainActivity))
        mRecyclerView.setAdapter(mTDAdapter);

    }

    override fun onStart() {
        super.onStart()
        mDownloader = AndroidDownloader(this);
        pasteButton.setOnClickListener {
            //Toast.makeText(baseContext, "URL" + pasteUrl, Toast.LENGTH_LONG).show();
            try {

                clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                if (!clipboardManager.hasPrimaryClip()) {
                    //  d("Clip Board data", "Not Valid Data");
                    if (!clipboardManager.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN)!!) displayToastless(
                        baseContext, "Not Valid Data"
                    );
                }
                pasteUrl = clipboardManager.primaryClip?.getItemAt(0)?.text as String;
                textFieldEnterUrl.setText(pasteUrl);
            } catch (e: Exception) {
                displayToastless(baseContext, "Can not get Copied Item");
            }
        }
        // Additional code to be executed when the activity starts
        downloadButton.setOnClickListener {
            if (pasteUrl.isEmpty()) displayToastless(baseContext, "Not valid Data");
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
            val dlink = TDService.tdInstance.getTdlink(urlId);

            //d("url calld", dlink.toString());
            dlink.enqueue(object : Callback<TDPojo> {
                override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()!!
                        val dlink = responseBody.dlink.toString();
                        mFileName = responseBody.server_filename.toString();
                        mFileSize = responseBody.size.toString();
                        //d("File name", mFileName);
                        startDownloadingFile(dlink);
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
        } else {
            displayToastless(baseContext, "Invalid Terabox URL");
        }
        //val link = textFieldEnterUrl.text.toString()
    }

    // Modify the startDownloadingFile function
    private fun startDownloadingFile(url: String) {
        mDownloadPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val file = File(mDownloadPath, mFileName)

        // Start the download and get the download ID
        val downloadId = mDownloader.downloadFile(url, mFileName, file)

        // Create an instance of DownloadStatusUtil for this download
        val downloadStatusUtil = DownloadStatusUtil(this, downloadTDDownloadModel)

        // Execute the download status monitoring
        downloadStatusUtil.execute(downloadId.toString())

        val downloadModel = TDDownloadModel()
        downloadModel.setmId(11)
        downloadModel.setmStatus("Downloading")
        downloadModel.setmFileName(mFileName)
        downloadModel.setmFileSize(mFileSize)
        downloadModel.setmProgress("0")
        downloadModel.setmIsPaused(false)
        downloadModel.setmDownloadId(downloadId)
        downloadModel.setmFilePath(mDownloadPath)

        // Add the download model to your list
        downloadTDDownloadModel.add(downloadModel)

        // Notify the adapter of the new download
        mTDAdapter.notifyItemInserted(downloadTDDownloadModel.size - 1)
        downloadStatusUtil.setOnProgressChangeListener { progress ->
            downloadModel.setmProgress(progress.toString())
            updateDownloadStatus(downloadModel)
        }
    }

    private fun updateDownloadStatus(downloadModel: TDDownloadModel) {
        val index =
            downloadTDDownloadModel.indexOfFirst { it.mDownloadId == downloadModel.mDownloadId }
        if (index != -1) {
            downloadTDDownloadModel[index] = downloadModel
            runOnUiThread {
                mTDAdapter.notifyItemChanged(index)
            }
        }
    }


    override fun onCLickItem(file_path: String?) {
    }

    override fun onShareClick(downloadModel: TDDownloadModel?) {
    }


//    val epochTimestamp = 1632591600L // Replace with your desired epoch timestamp
//    val formattedDate = convertEpochToDateTime(epochTimestamp)
//    println(formattedDate)

}

