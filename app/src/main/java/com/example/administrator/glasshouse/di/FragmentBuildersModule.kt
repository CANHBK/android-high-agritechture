package com.example.administrator.glasshouse.di

import com.example.administrator.glasshouse.ui.dashboard.AddGateBottomSheet
import com.example.administrator.glasshouse.ui.register.RegisterFragment
import com.example.administrator.glasshouse.ui.monitor.MonitorFragment
import com.example.administrator.glasshouse.ui.dashboard.DashboardFragment
import com.example.administrator.glasshouse.ui.login.LoginFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAddGateBottomSheet(): AddGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeMonitorFragment(): MonitorFragment

    @ContributesAndroidInjector
    abstract fun contributeGateFragment(): DashboardFragment
}
