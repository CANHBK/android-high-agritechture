package com.mandevices.iot.agriculture.di

import android.app.Application
import com.mandevices.iot.agriculture.util.FarmApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            ApolloModule::class,
            ThreadModule::class,
            HomeActivityModule::class,
            ViewModelModule::class,
        AppModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: FarmApp)
}
