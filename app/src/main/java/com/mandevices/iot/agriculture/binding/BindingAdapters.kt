package com.mandevices.iot.agriculture.binding

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.adapters.Converters
import com.mandevices.iot.agriculture.R
import com.mandevices.iot.agriculture.R.id.textView
import com.mandevices.iot.agriculture.vo.Relay
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.mandevices.iot.agriculture.vo.Sensor


/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean?) {
        view.visibility = if (show != null && show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleByRelay")
    fun visibleByRelay(view: View, relay: Relay?) {
        view.visibility = if (relay!!.onHour != null) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleIfListEmpty")
    fun <T> visibleIfListEmpty(view: View, lists: List<T>?) {
        view.visibility = if (lists == null || lists.isEmpty()) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleIfListNotEmpty")
    fun <T> visibleIfListNotEmpty(view: View, lists: List<T>?) {
        view.visibility = if (lists != null && !lists.isEmpty()) View.VISIBLE else View.GONE
    }


    @JvmStatic
    @BindingAdapter("error")
    fun setError(editText: TextInputEditText, strOrResId: Any?) {
        if (strOrResId != null) {
            if (strOrResId is Int) {
                editText.error = editText.context.getString(strOrResId)
            } else {
                editText.error = strOrResId as String
            }
        }
    }

    @JvmStatic
    @BindingAdapter("state")
    fun setState(textView: TextView, state: String) {
        textView.text = if (state == "F") "Đang TẮT" else "Đang BẬT"

    }

    @JvmStatic
    @BindingAdapter("text")
    fun setText(textView: TextView, value: Int) {
        textView.text = if (value != -1) value.toString() else "--"

    }

    @JvmStatic
    @BindingAdapter("mode")
    fun setMode(textView: TextView, relay: Relay) {
        textView.text = "${relay.onHour}:${relay.onMinute}"

    }

    @JvmStatic
    @BindingAdapter("timeOff")
    fun setTimeOff(textView: TextView, relay: Relay) {
        textView.text = "${relay.offHour}:${relay.offMinute}"

    }

    @JvmStatic
    @BindingAdapter("time")
    fun time(textView: TextView, sensor: Sensor) {
        textView.text = "${sensor.hour}:${sensor.minute}"

    }

    @JvmStatic
    @BindingAdapter("visibleIfRepeat")
    fun visibleIfRepeat(view: View, relay: Relay) {
        view.visibility = if (relay.isRepeat) View.VISIBLE else View.GONE

    }
    @JvmStatic
    @BindingAdapter("visibleIfPeriodic")
    fun visibleIfPeriodic(view: View, sensor: Sensor) {
        view.visibility = if (sensor.isPeriodic!=null && sensor.isPeriodic!!) View.VISIBLE else View.GONE

    }

    @JvmStatic
    @BindingAdapter("visibleIfSensorSetting")
    fun visibleIfSensorSetting(view: View, sensor: Sensor) {
        view.visibility = if (sensor.isPeriodic != null) View.VISIBLE else View.INVISIBLE

    }


    @JvmStatic
    @BindingAdapter("error")
    fun setError(editText: EditText, strOrResId: Any?) {
        if (strOrResId != null) {
            if (strOrResId is Int) {
                editText.error = editText.context.getString(strOrResId)
            } else {
                editText.error = strOrResId as String
            }
        }
    }

    @JvmStatic
    @BindingAdapter("onFocus")
    fun bindFocusChange(editText: TextInputEditText, onFocusChangeListener: View.OnFocusChangeListener) {
        if (editText.onFocusChangeListener == null) {
            editText.onFocusChangeListener = onFocusChangeListener
        }
    }

    @JvmStatic
    @BindingAdapter("onFocus")
    fun bindFocusChange(editText: EditText, onFocusChangeListener: View.OnFocusChangeListener) {
        if (editText.onFocusChangeListener == null) {
            editText.onFocusChangeListener = onFocusChangeListener
        }
    }

    @JvmStatic
    @BindingAdapter("textChangedListener")
    fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
        editText.addTextChangedListener(textWatcher)
    }
}
