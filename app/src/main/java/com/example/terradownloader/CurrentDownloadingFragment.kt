package com.example.terradownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.terradownloader.Adapter.QueuedAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [CurrentDownloadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentDownloadingFragment : Fragment() {
    private lateinit var viewModel: QueuedViewModel
    private lateinit var mQueuedAdapter: QueuedAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_downloading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(QueuedViewModel::class.java)

        // Assuming you have a RecyclerView with the id "recyclerView"
        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerview_fragment_currently_downloading)

        // Initialize the adapter and set it to the RecyclerView
        mQueuedAdapter = QueuedAdapter(viewModel.getDownloadItems())
        recyclerView.adapter = mQueuedAdapter

        // Update the adapter data when the ViewModel provides the actual data
//        viewModel.getDownloadItems().observe(viewLifecycleOwner, Observer { downloadItems ->
//            mQueuedAdapter.updateData(downloadItems)
//        })
    }

}
