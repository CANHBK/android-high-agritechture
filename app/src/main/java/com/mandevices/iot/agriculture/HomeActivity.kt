package com.mandevices.iot.agriculture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.paperdb.Paper
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Paper.init(applicationContext);

    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
