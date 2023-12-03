package com.example.terradownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


/**
 * A simple [Fragment] subclass.
 * Use the [CurrentDownloadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentDownloadingFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: QueuedViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_downloading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(QueuedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
