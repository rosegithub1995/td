package com.example.terradownloader.Adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.terradownloader.CurrentDownloadingFragment
import com.example.terradownloader.DownloadedFragment

class TabPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = CurrentDownloadingFragment()
                fragment.arguments = getBundleForFragment(position)
                fragment
            }
            1 -> {
                val fragment = DownloadedFragment()
                fragment.arguments = getBundleForFragment(position)
                fragment
            }
            else -> CurrentDownloadingFragment()
        }
    }

    private fun getBundleForFragment(position: Int): Bundle {
        val bundle = Bundle()
        // Pass any data you want to the fragments via arguments
        // For example, you can pass a ViewModel instance
        // bundle.putParcelable("viewModel", yourViewModel)
        return bundle
    }
}
