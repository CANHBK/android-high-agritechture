package com.mandevices.iot.agriculture.di

import com.mandevices.iot.agriculture.ui.dashboard.*
import com.mandevices.iot.agriculture.ui.register.RegisterFragment
import com.mandevices.iot.agriculture.ui.login.LoginFragment
import com.mandevices.iot.agriculture.ui.monitor.AddNodeBottomSheet
import com.mandevices.iot.agriculture.ui.monitor.DeleteNodeBottomSheet
import com.mandevices.iot.agriculture.ui.monitor.EditNodeBottomSheet
import com.mandevices.iot.agriculture.ui.monitor.MonitorFragment
import com.mandevices.iot.agriculture.ui.nodesettings.SensorSettingFragment

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


    //user
    @ContributesAndroidInjector
    abstract fun contributeUserProfileFragment(): UserBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

}
