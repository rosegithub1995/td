package com.example.terradownloader.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.Database.DownloadingTable
import com.example.terradownloader.R
import com.example.terradownloader.model.TDDownloadModel

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
        val name: TextView = itemView.findViewById(R.id.file_title)
        val status: TextView = itemView.findViewById(R.id.file_size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueuedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_download, parent, false)
        return QueuedViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueuedViewHolder, position: Int) {
        val currentItem = items[position]
        holder.name.text = currentItem.fileName
        holder.status.text = currentItem.fileName
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.let { it(currentItem) }
//        }

        holder.itemView.setOnClickListener {
            onStringItemClickClickListener?.let { it(currentItem.teraboxFileUrl) }
        }
    }

    fun setOnItemClickListener(listener: (DownloadingTable) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnStringItemClickListener(listener: (String) -> Unit) {
        onStringItemClickClickListener = listener
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

    private var onItemClickListener: ((DownloadingTable) -> Unit)? = null
    private var onStringItemClickClickListener: ((String) -> Unit)? = null
}
