package com.example.terradownloader.Adapter

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.R // Import your project's R class here
import com.example.terradownloader.model.TDDownloadModel

interface ItemClickListener {
    fun onCLickItem(filePath: String)
    fun onShareClick(downloadModel: TDDownloadModel)
}

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
        var file_title: TextView
        var file_size: TextView
        var file_progress: ProgressBar
        var pause_resume: Button

        //var sharefile: Button
        var file_status: TextView
        var main_rel: RelativeLayout

        init {
            file_title = itemView.findViewById(R.id.text_view_rc_file_name)
            file_size = itemView.findViewById(R.id.text_view_rc_file_size)
            file_status = itemView.findViewById(R.id.text_view_rc_file_status)
            file_progress = itemView.findViewById(R.id.progress_bar_rc_file_progress)
            pause_resume = itemView.findViewById(R.id.button_rc_pause_resume)
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
        holder.file_title.text = downloadModel.fileName
        holder.file_status.text = downloadModel.status
        holder.file_progress.progress = downloadModel.progress.toInt()
        holder.file_size.text = "Downloaded : " + downloadModel.fileSize
        if (downloadModel.getIsPaused()) {
            holder.pause_resume.text = "RESUME"
        } else {
            holder.pause_resume.text = "PAUSE"
        }
        if (downloadModel.status.equals("RESUME", ignoreCase = true)) {
            holder.file_status.text = "Running"
        }
        holder.pause_resume.setOnClickListener {
            if (downloadModel.getIsPaused()) {
                downloadModel.setIsPaused(false)
                holder.pause_resume.text = "PAUSE"
                downloadModel.setStatus("RESUME")
                holder.file_status.text = "Running"
                if (!resumeDownload(downloadModel)) {
                    Toast.makeText(context, "Failed to Resume", Toast.LENGTH_SHORT).show()
                }
                notifyItemChanged(position)
            } else {
                downloadModel.setIsPaused(true)
                holder.pause_resume.text = "RESUME"
                downloadModel.setStatus("PAUSE")
                holder.file_status.text = "PAUSE"
                if (!pauseDownload(downloadModel)) {
                    Toast.makeText(context, "Failed to Pause", Toast.LENGTH_SHORT).show()
                }
                notifyItemChanged(position)
            }
        }
        holder.main_rel.setOnClickListener { clickListener.onCLickItem(downloadModel.filePath) }
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
                arrayOf(downloadModel.getFileName())
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
                arrayOf(downloadModel.getFileName())
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
