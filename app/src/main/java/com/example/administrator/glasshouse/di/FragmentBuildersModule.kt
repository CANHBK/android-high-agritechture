package com.example.administrator.glasshouse.di

import com.example.administrator.glasshouse.ui.monitor.MonitorFragment
import com.example.administrator.glasshouse.ui.gate.GateFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMonitorFragment(): MonitorFragment

    @ContributesAndroidInjector
    abstract fun contributeGateFragment(): GateFragment
}
