package com.example.terradownloader

import QueuedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.Adapter.QueuedAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [DownloadedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadedFragment : Fragment() {


    private val viewModel: QueuedViewModel by activityViewModels()
    private lateinit var mQueuedAdapter: QueuedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bla_downloaded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerview_fragment_downloaded)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        //val mList = viewModel.getDownloadItems()

        mQueuedAdapter = QueuedAdapter() // Pass an empty list initially
        recyclerView.adapter = mQueuedAdapter


        viewModel.getQueuedListData().observe(viewLifecycleOwner) { downloadList ->
            if (downloadList.size > 3)
                mQueuedAdapter.updateAdapter(downloadList.subList(1, downloadList.size-1))

            Log.d("DownloadList lifecule", "Size: ${downloadList.size}")
        }
    }
}
