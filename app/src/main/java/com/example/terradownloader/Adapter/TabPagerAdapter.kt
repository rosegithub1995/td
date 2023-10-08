package com.example.terradownloader.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.terradownloader.CurrentDownloadingFragment
import com.example.terradownloader.DownloadedFragment

class TabPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentDownloadingFragment()
            1 -> DownloadedFragment()
            else -> CurrentDownloadingFragment()
        }
    }
}
