package com.example.administrator.glasshouse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.administrator.glasshouse.ViewModelFactory
import com.example.administrator.glasshouse.ui.dashboard.DashBoardViewModel
import com.example.administrator.glasshouse.ui.login.LoginViewModel
import com.example.administrator.glasshouse.ui.register.RegisterViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
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
