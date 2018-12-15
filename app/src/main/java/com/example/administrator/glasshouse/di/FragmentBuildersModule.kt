package com.example.administrator.glasshouse.di

import com.example.administrator.glasshouse.ui.dashboard.AddGateBottomSheet
import com.example.administrator.glasshouse.ui.register.RegisterFragment
import com.example.administrator.glasshouse.ui.dashboard.DashboardFragment
import com.example.administrator.glasshouse.ui.dashboard.DeleteGateBottomSheet
import com.example.administrator.glasshouse.ui.dashboard.EditGateBottomSheet
import com.example.administrator.glasshouse.ui.login.LoginFragment
import com.example.administrator.glasshouse.ui.monitor.AddNodeBottomSheet
import com.example.administrator.glasshouse.ui.monitor.DeleteNodeBottomSheet
import com.example.administrator.glasshouse.ui.monitor.EditNodeBottomSheet
import com.example.administrator.glasshouse.ui.monitor.MonitorFragment
import com.example.administrator.glasshouse.ui.nodesettings.SensorSettingFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

//    monitor

    @ContributesAndroidInjector
    abstract fun contributeAddNodeBottomSheet(): AddNodeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeEditNodeBottomSheet(): EditNodeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeDeleteNodeBottomSheet(): DeleteNodeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeMonitorFragment(): MonitorFragment



//    gate
    @ContributesAndroidInjector
    abstract fun contributeAddGateBottomSheet(): AddGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeEditGateBottomSheet(): EditGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeDeleteGateBottomSheet(): DeleteGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeGateFragment(): DashboardFragment


    //sensor
    @ContributesAndroidInjector
    abstract fun contributeSensorSettingFragment(): SensorSettingFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

}
