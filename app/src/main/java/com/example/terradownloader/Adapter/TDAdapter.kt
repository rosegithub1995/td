package com.example.terradownloader.Adapter

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.R // Import your project's R class here
import com.example.terradownloader.interfaces.ItemClickListener
import com.example.terradownloader.model.TDDownloadModel
import org.w3c.dom.Text

class TDAdapter(
    var context: Context,
    downloadModels: List<TDDownloadModel>,
    itemClickListener: ItemClickListener
) : RecyclerView.Adapter<TDAdapter.DownloadViewHolder>() {

    var downloadModels: List<TDDownloadModel> = ArrayList()
    var clickListener: ItemClickListener

    init {
        clickListener = itemClickListener
        this.downloadModels = downloadModels
    }

    inner class DownloadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textViewFileTitle: TextView
        lateinit var textViewFileSize: TextView
        lateinit var textViewFilePercentageCompleted: TextView;
        lateinit var progressBarFileProgress: ProgressBar
        lateinit var buttonPauseResume: Button

        //var sharefile: Button
        lateinit var textViewFileStatus: TextView
        var main_rel: LinearLayout

        init {
            textViewFileTitle = itemView.findViewById(R.id.text_view_rc_file_name)
            textViewFileSize = itemView.findViewById(R.id.text_view_rc_file_size)
            textViewFileStatus = itemView.findViewById(R.id.text_view_rc_file_status)
            progressBarFileProgress = itemView.findViewById(R.id.progress_bar_rc_file_progress)
            textViewFilePercentageCompleted =
                itemView.findViewById(R.id.text_view_rc_file_percentage_completed)
            buttonPauseResume = itemView.findViewById(R.id.button_rc_pause_resume)
            main_rel = itemView.findViewById(R.id.main_rel)
            //sharefile = itemView.findViewById(R.id.sharefile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.rc_layout, parent, false)
        return DownloadViewHolder(view)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        val downloadModel: TDDownloadModel = downloadModels[position]
        holder.textViewFileTitle.text = downloadModel.mFileName
        holder.textViewFileStatus.text = downloadModel.mStatus
        holder.progressBarFileProgress.progress = downloadModel.mProgress.toInt()
        if (downloadModel.mProgress.toInt() == 100) {
            holder.buttonPauseResume.visibility = View.GONE;
        }
        holder.textViewFilePercentageCompleted.text =
            downloadModel.mProgress.toInt().toString() + "% Completed";
        holder.textViewFileSize.text = "Size : " + downloadModel.mFileSize
        if (downloadModel.getmIsPaused()) {
            holder.buttonPauseResume.text = "Resume"
        } else {
            holder.buttonPauseResume.text = "Pause"
        }
        if (downloadModel.mStatus.equals("Resume", ignoreCase = true)) {
            holder.textViewFileStatus.text = "Downloading"
        }
        holder.buttonPauseResume.setOnClickListener {
            if (downloadModel.getmIsPaused()) {
                downloadModel.setmIsPaused(false)
                holder.buttonPauseResume.text = "Pause"
                downloadModel.setmStatus("Resume")
                holder.textViewFileStatus.text = "Downloading"
                if (!resumeDownload(downloadModel)) {
                    Toast.makeText(context, "Failed to Resume", Toast.LENGTH_SHORT).show()
                }
                notifyItemChanged(position)
            } else {
                downloadModel.setmIsPaused(true)
                holder.buttonPauseResume.text = "Resume"
                downloadModel.setmStatus("Pause")
                holder.textViewFileStatus.text = "Pause"
                if (!pauseDownload(downloadModel)) {
                    Toast.makeText(context, "Failed to Pause", Toast.LENGTH_SHORT).show()
                }
                notifyItemChanged(position)
            }
        }
        holder.main_rel.setOnClickListener { clickListener.onCLickItem(downloadModel.mFilePath) }
        //holder.sharefile.setOnClickListener { clickListener.onShareClick(downloadModel) }
    }

    private fun pauseDownload(downloadModel: TDDownloadModel): Boolean {
        var updatedRow = 0
        val contentValues = ContentValues()
        contentValues.put("control", 1)
        try {
            updatedRow = context.contentResolver.update(
                Uri.parse("content://downloads/my_downloads"),
                contentValues,
                "title=?",
                arrayOf(downloadModel.getmFileName())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return updatedRow > 0
    }

    private fun resumeDownload(downloadModel: TDDownloadModel): Boolean {
        var updatedRow = 0
        val contentValues = ContentValues()
        contentValues.put("control", 0)
        try {
            updatedRow = context.contentResolver.update(
                Uri.parse("content://downloads/my_downloads"),
                contentValues,
                "title=?",
                arrayOf(downloadModel.getmFileName())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return updatedRow > 0
    }

    override fun getItemCount(): Int {
        return downloadModels.size
    }
}

//    fun changeItem(downloadid: Long) {
//        var i = 0
//        for (downloadModel in downloadModels) {
//            if (downloadid == downloadModel.downloadId) {
//                notifyItemChanged(i)
//            }
//            i++
//        }
//    }
//
//    fun ChangeItemWithStatus(message: String?, downloadid: Long): Boolean {
//        var comp = false
//        var i = 0
//        for (downloadModel in downloadModels) {
//            if (downloadid == downloadModel.downloadId) {
//                val realm: Realm = Realm.getDefaultInstance()
//                val finalI = i
//                realm.executeTransaction {
//                    if (message != null) {
//                        downloadModels[finalI].setStatus(message)
//                    }
//                    notifyItemChanged(finalI)
//                    realm.copyToRealmOrUpdate(downloadModels[finalI])
//                }
//                comp = true
//            }
//            i++
//        }
//        return comp
//    }
//
//    fun setChangeItemFilePath(path: String?, id: Long) {
//        val realm: Realm = Realm.getDefaultInstance()
//        var i = 0;
//        for (downloadModel in downloadModels) {
//            if (id == downloadModel.downloadId) {
//                val finalI = i
//                realm.executeTransaction {
//                    if (path != null) {
//                        downloadModels[finalI].setFilePath(path)
//                    }
//                    notifyItemChanged(finalI)
//                    realm.copyToRealmOrUpdate(downloadModels[finalI])
//                }
//            }
//            i++
//        }
//    }
//}
