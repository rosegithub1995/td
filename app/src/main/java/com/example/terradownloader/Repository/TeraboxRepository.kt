package com.example.terradownloader.Repository

import TDDownloadModel
import android.content.Context
import android.os.Environment
import android.util.Log.d
import androidx.lifecycle.LiveData
import com.example.terradownloader.Database.DBTeraboxDatabase
import com.example.terradownloader.Database.DownloadDAO
import com.example.terradownloader.interfaces.TDInterface
import com.example.terradownloader.model.TDPojo
import com.example.terradownloader.utils.AndroidDownloader
import com.example.terradownloader.utils.Tdutils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date

class TeraboxRepository(
    private val tdInterface: TDInterface, private val mContext: Context,
   private val database: DBTeraboxDatabase
) {
    private var mDownloadPath: String = "downloads";
    private var mDownloader: AndroidDownloader = AndroidDownloader(mContext);
    private var pasteUrl: String = ""
    private var job: Job? = null
    private var previousProgress = 0 // Initialize with 0
    private lateinit var downloadDAO: DownloadDAO


    var dlink = "";
    var mFileName = "dummyFileName"
    var mFileSize = ""
    var url1 = ""
    var url2 = ""
    var url3 = ""
    var local_c_time = ""
    var local_m_time = ""
    lateinit var downloadModel: TDDownloadModel


    /**
     * This function is going to be called from the ViewModel
     */
    suspend fun getTDResponseFromInternetApiCallRepository(urlId: String): TDDownloadModel {
        val dlinkFetchResponse = TDInterface.getTDRetrofitInstance().getTdlink(urlId);
        //d("url calld", dlink.toString());
        dlinkFetchResponse.enqueue(object : Callback<TDPojo> {
            override fun onResponse(call: Call<TDPojo>, response: Response<TDPojo>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    dlink = responseBody.dlink.toString();
                    mFileName = responseBody.server_filename.toString();
                    mFileSize = responseBody.size.toString();
                    url1 = responseBody.thumbs.url1.toString();
                    url2 = responseBody.thumbs.url2.toString();
                    url3 = responseBody.thumbs.url3.toString();
                    local_c_time = responseBody.local_ctime.toString();
                    local_m_time = responseBody.local_mtime.toString();
                    //d("File name", mFileName);
                    downloadModel = TDDownloadModel()
                    downloadModel = startDownloadingFile(dlink, mFileName, mFileSize);
                    downloadDAO=database.downloadedDAO();
                    CoroutineScope(Dispatchers.IO).launch {
                        downloadDAO.insertCurrentlyDownloadingItemToDatabase(downloadModel)
                    }
                    //d("Response data", responseBody.toString());
                    //d("d link", dlink);
                }
            }

            override fun onFailure(call: Call<TDPojo>, t: Throwable) {
                d("Error in retrofit call", "Retrofit call error ", t);
                Tdutils.displayToastless(mContext, "Error in Fetching File Url")
            }
        })
        return downloadModel;

    }


    //Get all details from the Room db
    suspend fun getTDResponseFromRoomDBRepository(): LiveData<List<TDDownloadModel>> {
        return downloadDAO.getCurrentlyDownloadingItemFromDatabase()
    }


    //    Modify the startDownloadingFile function
    fun startDownloadingFile(
        url: String,
        mFileName: String,
        mFileSize: String
    ): TDDownloadModel {
        mDownloadPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val file = File(mDownloadPath, this.mFileName)

        // Start the download and get the download ID from Download Manager
        val downloadId = mDownloader.downloadFile(url, this.mFileName, file)

        // Create an instance of DownloadStatusUtil for this download
        //val downloadStatusUtil = DownloadStatusUtil(mContext, downloadTDDownloadModel)

        // Execute the download status monitoring
        //downloadStatusUtil.execute(downloadId.toString())

        val downloadModel = TDDownloadModel()
        downloadModel.teraboxFileUrl = pasteUrl
        downloadModel.downloadFileUrl = url
        downloadModel.idCurrentlyDownloadingFromManager = downloadId
        downloadModel.downloadStatus = TDDownloadModel.DownloadStatus.STARTED
        downloadModel.fileName = mFileName
        downloadModel.fileSize = mFileSize
        downloadModel.progress = "0"
        downloadModel.isPaused = false
        downloadModel.filePath = mDownloadPath
        downloadModel.thumbnailUrl1 = url1
        downloadModel.thumbnailUrl2 = url2
        downloadModel.thumbnailUrl3 = url3
        downloadModel.downloadStartingDate = Date()
        downloadModel.downloadFinishingDate = Date()
        downloadModel.fileUploadDate = local_c_time.toLong()

        return downloadModel;


        // Add the download model to your list
        //mTDResponseFromTeraUrl.add(0, downloadModel)
        //mMyDatabaseHelper.addToTeraboxDatabase(this, downloadModel)

        // Notify the adapter of the new download
//        mTDAdapter.notifyItemInserted(downloadTDDownloadModel.size - 1)
//        downloadStatusUtil.setOnProgressChangeListener { progress ->
//            downloadModel.setmProgress(progress.toString())
//            updateDownloadStatus(downloadModel)
//        }
    }


}
