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
    @BindingAdapter("mode")
    fun setMode(textView: TextView, relay: Relay) {
        textView.text = if (relay.isPeriodic) "Tự động - ${relay.hour} - ${relay.minute}" else "Thủ công"

    }
    @JvmStatic
    @BindingAdapter("app:srcVector")
    fun setSrcVector(view: ImageView, @DrawableRes drawable: Int) {
        view.setImageDrawable(ContextCompat.getDrawable(view.context,drawable))
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
