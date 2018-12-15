package com.mandevices.iot.agriculture.ui.control


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.databinding.FragmentRelayControlBinding
import com.mandevices.iot.agriculture.ui.dashboard.AddGateBottomSheet
import com.mandevices.iot.agriculture.ui.dashboard.DeleteGateBottomSheet
import com.mandevices.iot.agriculture.ui.dashboard.EditGateBottomSheet
import com.mandevices.iot.agriculture.ui.dashboard.UserBottomSheet


class ControlFragment : Fragment() {

    private var addBottomSheet: AddGateBottomSheet? = null
    private var deleteBottomSheet: DeleteGateBottomSheet? = null
    private var editBottomSheet: EditGateBottomSheet? = null
    private var userBottomSheet: UserBottomSheet? = null

    private lateinit var binding: FragmentRelayControlBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_control, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }


}
