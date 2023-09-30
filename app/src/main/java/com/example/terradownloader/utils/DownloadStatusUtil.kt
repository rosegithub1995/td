import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.util.Log.d
import com.example.terradownloader.model.TDDownloadModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DownloadStatusUtil(
    private val context: Context,
    private val mDownloadModel: MutableList<TDDownloadModel>
) {

    private var job: Job? = null

    fun execute(downloadId: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            downloadFileProcess(downloadId)
        }
    }

    @SuppressLint("Range")
    private suspend fun downloadFileProcess(downloadId: String) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var downloading = true

        while (downloading) {
            val query = DownloadManager.Query()
            query.setFilterById(downloadId.toLong())
            val cursor: Cursor = downloadManager.query(query)
            cursor.moveToFirst()

            val bytesDownloaded =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val totalSize =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }
            //d("Downloading progress", bytesDownloaded.toString() + "%")
            //d("Downloading total size", totalSize.toString())

            val progress = ((bytesDownloaded * 100L) / totalSize).toInt()
            val status = getStatusMessage(cursor)

            val formattedFileSize = bytesIntoHumanReadable(totalSize.toLong())

            bytesIntoHumanReadable(bytesDownloaded.toLong())?.let {
                status?.let { it1 ->
                    val dataModelToUpdate =
                        mDownloadModel.find { it.mDownloadId == downloadId.toLong() }
                    if (dataModelToUpdate != null) {
                        dataModelToUpdate.setmProgress(progress.toString())
                        if (formattedFileSize != null) {
                            dataModelToUpdate.setmFileSize(formattedFileSize)
                        }
                        if(progress==100){
                            dataModelToUpdate.setmStatus("Downloaded");
                        }
                    }
                    if (formattedFileSize != null) {
                        updateRealm(
                            progress.toString(), formattedFileSize, it1
                        )
                    }
                }
            }
            progressChangeListener?.invoke(progress.toString())

            //d("File Status %", progress.toString())

            cursor.close()
        }
    }

    private var progressChangeListener: ((String) -> Unit)? = null

    fun setOnProgressChangeListener(listener: (String) -> Unit) {
        progressChangeListener = listener
    }

    private fun bytesIntoHumanReadable(bytes: Long): String? {
        val kilobyte: Long = 1024
        val megabyte = kilobyte * 1024
        return when {
            bytes < kilobyte -> "$bytes B"
            bytes < megabyte -> String.format("%.2f KB", bytes.toFloat() / kilobyte)
            else -> String.format("%.2f MB", bytes.toFloat() / megabyte)
        }
    }

    @SuppressLint("Range")
    private fun getStatusMessage(cursor: Cursor): String? {
        var msg = "-"
        msg = when (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            DownloadManager.STATUS_FAILED -> "Failed"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_RUNNING -> "Running"
            DownloadManager.STATUS_SUCCESSFUL -> "Completed"
            DownloadManager.STATUS_PENDING -> "Pending"
            else -> "Unknown"
        }
        return msg
    }

    private suspend fun updateRealm(progress: String, fileSize: String, status: String) {
        withContext(Dispatchers.IO) {
            // Update your realm data here
        }
    }
}
