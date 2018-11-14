package com.example.administrator.glasshouse.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.administrator.glasshouse.ChartFragment
import com.example.administrator.glasshouse.NotiFragment
import com.example.administrator.glasshouse.OverviewFragment
import com.example.administrator.glasshouse.RelayFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                val fragmentOverview = OverviewFragment()
                return fragmentOverview
            }
            1 -> {
                val fragmentRelay = RelayFragment()
                return fragmentRelay
            }
            2 -> {
                val fragmentChart = ChartFragment()
                return fragmentChart
            }
            3 -> {
                val fragmentNoti = NotiFragment()
                return fragmentNoti
            }
            else -> {
                return null
            }
        }
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {return "Overview"}
            1 -> {return "Relay"}
            2 -> {return "Chart"}
            3 -> {return "Notis"}
        }
        return super.getPageTitle(position)
    }
}