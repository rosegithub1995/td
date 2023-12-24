
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


class CurrentDownloadingFragment : Fragment() {
    private val viewModel: QueuedViewModel by activityViewModels()
    private lateinit var mQueuedAdapter: QueuedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_downloading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your ViewModel using the activity's ViewModelStore

        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerview_fragment_currently_downloading)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        //val mList = viewModel.getDownloadItems()

        mQueuedAdapter = QueuedAdapter() // Pass an empty list initially
        recyclerView.adapter = mQueuedAdapter


        viewModel.getQueuedListData().observe(viewLifecycleOwner) { downloadList ->
            if (downloadList.size > 0) {
                view.findViewById<View>(R.id.textview_no_items).visibility = View.GONE
            }
            mQueuedAdapter.updateAdapter(downloadList)
            Log.d("DownloadList lifecule", "Size: ${downloadList.size}")
        }


        //Add the data to recyclerview


    }
}
