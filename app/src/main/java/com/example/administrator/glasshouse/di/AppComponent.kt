package com.example.administrator.glasshouse.di

import android.app.Application
import com.example.administrator.glasshouse.util.FarmApp
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
