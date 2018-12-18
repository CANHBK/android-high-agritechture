package com.mandevices.iot.agriculture.ui.chart

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mandevices.iot.agriculture.di.Injectable
import javax.inject.Inject

class FragmentChart : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


}