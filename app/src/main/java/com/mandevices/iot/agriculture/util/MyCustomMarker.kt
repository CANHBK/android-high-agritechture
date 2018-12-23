package com.mandevices.iot.agriculture.util

import android.content.Context
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.View
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.mandevices.iot.agriculture.R


class MyCustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView

    // this will center the marker-view horizontally
    val xOffset: Int
        get() = -(width / 2)

    // this will cause the marker-view to be above the selected value
    val yOffset: Int
        get() = -height

    init {
        // this markerview only displays a textview
        tvContent = findViewById<View>(R.id.tvContent) as TextView
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvContent.text = "${e.y}" // set the entry-value as the display text
    }
}