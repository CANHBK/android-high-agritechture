package com.mandevices.iot.agriculture.di

import com.mandevices.iot.agriculture.ui.chart.FragmentChart
import com.mandevices.iot.agriculture.ui.control.AddControlBottomSheet
import com.mandevices.iot.agriculture.ui.control.ControlFragment
import com.mandevices.iot.agriculture.ui.control.DeleteControlBottomSheet
import com.mandevices.iot.agriculture.ui.control.EditControlBottomSheet
import com.mandevices.iot.agriculture.ui.dashboard.*
import com.mandevices.iot.agriculture.ui.register.RegisterFragment
import com.mandevices.iot.agriculture.ui.login.LoginFragment
import com.mandevices.iot.agriculture.ui.monitor.*
import com.mandevices.iot.agriculture.ui.nodesettings.SensorSettingFragment
import com.mandevices.iot.agriculture.ui.relaysettings.RelaySettingFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

//    monitor

    @ContributesAndroidInjector
    abstract fun contributeAddNodeBottomSheet(): AddNodeBottomSheet
    @ContributesAndroidInjector
    abstract fun contributeSetTimeBottomSheet(): SetTimeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeEditNodeBottomSheet(): EditNodeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeDeleteNodeBottomSheet(): DeleteNodeBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeMonitorFragment(): MonitorFragment

    @ContributesAndroidInjector
    abstract fun contributeSensorSettingFragment(): SensorSettingFragment

    @ContributesAndroidInjector
    abstract fun contributeChartFragment(): FragmentChart


    //    control
    @ContributesAndroidInjector
    abstract fun contributeAddControlBottomSheet(): AddControlBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeEditControlBottomSheet(): EditControlBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeDeleteControlBottomSheet(): DeleteControlBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeControlFragment(): ControlFragment

    @ContributesAndroidInjector
    abstract fun contributeRelaySettingFragment(): RelaySettingFragment



//    gate
    @ContributesAndroidInjector
    abstract fun contributeAddGateBottomSheet(): AddGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeEditGateBottomSheet(): EditGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeDeleteGateBottomSheet(): DeleteGateBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeGateFragment(): DashboardFragment


    //user
    @ContributesAndroidInjector
    abstract fun contributeUserProfileFragment(): UserBottomSheet

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

}
