import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log.d
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.Adapter.TDAdapter
import com.example.terradownloader.utils.Tdutils;
import com.example.terradownloader.utils.Tdutils.displayToastless

class DownloadCompleteReceiver : BroadcastReceiver() {
    private lateinit var mDownloadManager: DownloadManager
    private lateinit var downloadTDDownloadModel: MutableList<TDDownloadModel>
    private lateinit var mTDAdapter: TDAdapter
    private lateinit var myDatabaseHelper: MyDatabaseHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        myDatabaseHelper = context?.let { MyDatabaseHelper(it) } ?: return
        mDownloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                // Find the corresponding download model and update its status
                val downloadModel = findDownloadModelById(id)
                if (downloadModel != null) {
                    downloadModel.setmStatus("Completed")
                    // Update any other relevant information
                    // For example: downloadModel.setmProgress("100 % Downloaded")
                    // ...
                    // Notify your adapter of the changes
                    val position = downloadTDDownloadModel.indexOf(downloadModel)
                    if (position != -1) {
                        mTDAdapter.notifyItemChanged(position)
                    }

                    displayToastless(context, "Download completed successfully")
                    d("Download ID finished", id.toString())
                    myDatabaseHelper.updateToTeraboxDatabase(context, downloadModel);
                }
            }
            context?.sendBroadcast(Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE"))
        }
    }

    private fun findDownloadModelById(downloadId: Long): TDDownloadModel? {
        // Iterate through your downloadTDDownloadModel list and find the corresponding model
        return downloadTDDownloadModel.find { it.mDownloadId == downloadId }
    }
}
