
package com.example.terradownloader
import QueuedViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.Adapter.QueuedAdapter


class CurrentDownloadingFragment : Fragment() {
    private lateinit var viewModel: QueuedViewModel
    private lateinit var mQueuedAdapter: QueuedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_downloading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(QueuedViewModel::class.java)

        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerview_fragment_currently_downloading)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        //val mList = viewModel.getDownloadItems()

        mQueuedAdapter = QueuedAdapter() // Pass an empty list initially
        recyclerView.adapter = mQueuedAdapter


        viewModel.downloadList.observe(viewLifecycleOwner) { downloadList ->
            Log.d("DownloadList lifecule", "Size: ${downloadList.size}")

        }
        val mList = viewModel.getDownloadItems()
        if (mList.value == null) {
            Log.d("DownloadList frag ", "null")
        } else {
            Log.d("DownloadList frag ", "not null")
            for (i in mList.value!!) {

                Log.d("DownloadList frag ", i.fileName)
            }
        }


        //Add the data to recyclerview


    }
}
