package com.example.terradownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


/**
 * A simple [Fragment] subclass.
 * Use the [DownloadedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DownloadedFragment : Fragment() {

    private lateinit var viewModel: DownloadedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bla_downloaded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DownloadedViewModel::class.java)

    }
}
