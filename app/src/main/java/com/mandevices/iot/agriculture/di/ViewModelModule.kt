package com.mandevices.iot.agriculture.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mandevices.iot.agriculture.util.ViewModelFactory
import com.mandevices.iot.agriculture.ui.dashboard.DashBoardViewModel
import com.mandevices.iot.agriculture.ui.dashboard.UserViewModel
import com.mandevices.iot.agriculture.ui.login.LoginViewModel
import com.mandevices.iot.agriculture.ui.control.MonitorViewModel
import com.mandevices.iot.agriculture.ui.nodesettings.SensorSettingViewModel
import com.mandevices.iot.agriculture.ui.register.RegisterViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(userViewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SensorSettingViewModel::class)
    abstract fun bindSensorSettingViewModel(sensorSettingViewModel: SensorSettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonitorViewModel::class)
    abstract fun bindMonitorViewModel(monitorViewModel: MonitorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashBoardViewModel::class)
    abstract fun bindGateViewModel(dashBoardViewModel: DashBoardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory



}
