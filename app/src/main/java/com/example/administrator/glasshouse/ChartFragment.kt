package com.example.administrator.glasshouse

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.mikephil.charting.charts.LineChart
import org.angmarch.views.NiceSpinner

class ChartFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.chart_fragment_layout, container, false)
        val spinPara = view.findViewById(R.id.spinThongSo) as NiceSpinner
        val spintime = view.findViewById(R.id.spinTime) as NiceSpinner
        val chart = view.findViewById(R.id.chart) as LineChart

        // thiết lập cho 2 spinner
        val timeAdapter = ArrayAdapter.createFromResource(context!!, R.array.Time, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spintime.setAdapter(timeAdapter)
        spintime.showArrow()
        val paraAdapter = ArrayAdapter.createFromResource(context!!, R.array.Parameter, android.R.layout.simple_spinner_item)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinPara.setAdapter(paraAdapter)
        spinPara.showArrow()

        // Đổ thử dữ liệu lên LineChart

        return view
    }
}