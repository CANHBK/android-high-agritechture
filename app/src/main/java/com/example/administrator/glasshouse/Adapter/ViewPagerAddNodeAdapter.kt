package com.example.administrator.glasshouse.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.administrator.glasshouse.Fragment.AddRelayFragment
import com.example.administrator.glasshouse.Fragment.AddSensorFragment

class ViewPagerAddNodeAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when(position){
            0 -> {
                val addSensor = AddSensorFragment()
                addSensor
            }
            1 -> {
                val addRelay = AddRelayFragment()
                addRelay
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> {
                "Add Node Sensor"
            }
            1 -> {
                "Add Node Relay"
            }
            else -> ""
        }
    }
}