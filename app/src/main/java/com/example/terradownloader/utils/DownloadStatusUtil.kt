
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DownloadStatusUtil(
    private val mContext: Context,
    private val mDownloadModelList: MutableList<TDDownloadModel>
) {
    private var job: Job? = null
    private var previousProgress = 0 // Initialize with 0

    fun execute(downloadId: Long) {
        job = CoroutineScope(Dispatchers.IO).launch {
            downloadFileProgress(downloadId, mDownloadModelList)
        }
    }

    @SuppressLint("Range")
    private suspend fun downloadFileProgress(
        downloadId: Long,
        mDownloadModel: MutableList<TDDownloadModel>
    ) {
        val downloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var downloading = true

        while (downloading) {
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor: Cursor = downloadManager.query(query)
            cursor.moveToFirst()

            val bytesDownloaded =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val totalSize =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }

            val progress = ((bytesDownloaded * 100L) / totalSize).toInt()
            val status = getStatusMessage(cursor)

            val formattedFileSize = bytesIntoHumanReadable(totalSize.toLong())

            bytesIntoHumanReadable(bytesDownloaded.toLong())?.let {
                status?.let { it1 ->
                    val dataModelToUpdate =
                        mDownloadModel.find { it.idCurrentlyDownloadingFromManager == downloadId }
                    if (dataModelToUpdate != null) {
                        dataModelToUpdate.progress = progress.toString()
                        if (formattedFileSize != null) {
                            dataModelToUpdate.fileSize = formattedFileSize
                        }
                        if (progress == 100) {
                            dataModelToUpdate.downloadStatus = "Downloaded"
                        }
                    }
                }
            }

            // Check if progress has changed before invoking the listener
            if (progress != previousProgress) {
                progressChangeListener?.invoke(progress.toString())
                previousProgress = progress // Update the previous progress
            }
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
}
