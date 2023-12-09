package com.example.terradownloader.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.R
import com.example.terradownloader.model.TDDownloadModel

class QueuedAdapter(private val downloadItems: List<TDDownloadModel>) :
    RecyclerView.Adapter<QueuedAdapter.QueuedViewHolder>() {

    class QueuedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textViewFileName)
        val status: TextView = itemView.findViewById(R.id.textViewFileSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueuedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_download, parent, false)
        return QueuedViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueuedViewHolder, position: Int) {
        val currentItem = downloadItems[position]
        holder.name.text = currentItem.fileName
        holder.status.text = currentItem.fileSize
    }

    override fun getItemCount() = downloadItems.size
}
