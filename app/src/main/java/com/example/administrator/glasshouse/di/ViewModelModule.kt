package com.example.administrator.glasshouse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.administrator.glasshouse.ViewModelFactory
import com.example.administrator.glasshouse.api.GraphQL
import com.example.administrator.glasshouse.db.GateDao
import com.example.administrator.glasshouse.repository.GateRepository
import com.example.administrator.glasshouse.ui.gate.GateViewModel
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
    @ViewModelKey(GateViewModel::class)
    abstract fun bindGateViewModel(gateViewModel: GateViewModel): ViewModel

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
