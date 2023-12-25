package com.example.terradownloader.Adapter

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.terradownloader.R
import com.example.terradownloader.model.TDDownloadModel
import com.example.terradownloader.utils.Tdutils

class QueuedAdapter :
    RecyclerView.Adapter<QueuedAdapter.QueuedViewHolder>() {
    private var items: MutableList<TDDownloadModel> = mutableListOf()


//    private var differCallBack = object : DiffUtil.ItemCallback<DownloadingTable>() {
//        override fun areItemsTheSame(
//            oldItem: DownloadingTable,
//            newItem: DownloadingTable
//        ): Boolean {
//            return oldItem.teraboxFileUrl == newItem.teraboxFileUrl
//        }
//
//        override fun areContentsTheSame(
//            oldItem: DownloadingTable,
//            newItem: DownloadingTable
//        ): Boolean {
//
//            return oldItem == newItem
//        }
//
//    }
//    private var stringDifferCallback = object : DiffUtil.ItemCallback<String>() {
//        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//
//            return oldItem == newItem
//        }
//
//    }

//    val differ = androidx.recyclerview.widget.AsyncListDiffer(this, differCallBack)
//    val stringDiffer = AsyncListDiffer(this, stringDifferCallback)

    inner class QueuedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_file_icon)
        val name: TextView = itemView.findViewById(R.id.tv_file_name)
        val size: TextView = itemView.findViewById(R.id.tv_file_size)
        val progressBarView: ProgressBar = itemView.findViewById(R.id.file_progress)
        val retryCancel: ImageView = itemView.findViewById(R.id.iv_file_cancel)
        val filePercent: TextView = itemView.findViewById(R.id.tv_file_percent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueuedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_download, parent, false)
        return QueuedViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueuedViewHolder, position: Int) {
        val currentItem = items[position]
        holder.name.text = currentItem.fileName
        holder.size.text = "Size: " + currentItem.fileSize
        val status = currentItem.downloadStatus
        if (status != Tdutils.STRING_FETCHING) {
            holder.progressBarView.isIndeterminate = false
            holder.progressBarView.progress = currentItem.progress.toInt()
        } else {
            holder.progressBarView.isIndeterminate = true
        }
        if (status == Tdutils.STRING_FAILED) {
            holder.retryCancel.setImageResource(R.drawable.refresh_24)
        } else {
            holder.retryCancel.setImageResource(com.google.android.material.R.drawable.mtrl_ic_cancel)
        }
        holder.filePercent.text = currentItem.progress.toInt().toString() + "% Completed"

        Glide.with(holder.itemView.context).load(currentItem.thumbnailUrl1)
            .placeholder(R.drawable.baseline_image_24)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
            // You can perform additional actions here if needed
            Toast.makeText(
                holder.itemView.context,
                "Clicked ${currentItem.teraboxFileUrl}",
                Toast.LENGTH_SHORT
            ).show()
            d("Clicked", "Clicked ${currentItem.teraboxFileUrl}")
        }
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.let { it(currentItem) }
//        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }

        //if Cancel button is clicked
        holder.retryCancel.setOnClickListener {
            //remove the item from the list and remove from the DB
        }
    }


    override fun getItemCount(): Int {
        //
        //differ.currentList.size
        return items.size
    }
    fun updateAdapter(newList: List<TDDownloadModel>) {
        // Update the internal list of the adapter
        items = newList.toMutableList()
        notifyDataSetChanged()
    }


    //ClickListener

    private var onItemClickListener: ((TDDownloadModel) -> Unit)? = null
}
