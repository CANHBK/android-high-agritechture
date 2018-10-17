package com.example.administrator.glasshouse

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                val fragmentOverview = OverviewFragment()
                return fragmentOverview
            }
            1 -> {
                val fragmentChart = ChartFragment()
                return fragmentChart
            }
            2 -> {
                val fragmentNoti = NotiFragment()
                return fragmentNoti
            }
            else -> {
                return null
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}