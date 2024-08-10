package com.example.dailytransac.frame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.dailytransac.R
import com.example.dailytransac.kuna.ViewPagerAdapter
import com.example.dailytransac.transaction.daily
import com.example.dailytransac.transaction.monthly
import com.example.dailytransac.transaction.yearly
import com.google.android.material.tabs.TabLayout

class analysis : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_analysis, container, false)

        adddataframe(view)

        return view
    }

    private fun adddataframe(view: View) {
        tabLayout = view.findViewById(R.id.analysis_data_checker)
        viewPager = view.findViewById(R.id.analysis_view_data)
        val adapter1 = ViewPagerAdapter(
            childFragmentManager
        )
        adapter1.addFragment(daily(),"Daily")
        adapter1.addFragment(monthly(),"Monthly")
        adapter1.addFragment(yearly(),"Yearly")
        viewPager.adapter = adapter1
        tabLayout.setupWithViewPager(viewPager)
    }
}