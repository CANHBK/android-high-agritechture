package com.example.administrator.glasshouse.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.administrator.glasshouse.Fragment.ChartFragment
import com.example.administrator.glasshouse.Fragment.NotiFragment
import com.example.administrator.glasshouse.Fragment.MonitorFragment
import com.example.administrator.glasshouse.Fragment.ControlFragment

class ViewPagerFarmAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                val fragmentOverview = MonitorFragment()
                return fragmentOverview
            }
            1 -> {
                val fragmentRelay = ControlFragment()
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