package com.example.administrator.glasshouse.binding

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
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
    @BindingAdapter("onFocus")
    fun bindFocusChange(editText: TextInputEditText, onFocusChangeListener: View.OnFocusChangeListener) {
        if (editText.onFocusChangeListener == null) {
            editText.onFocusChangeListener = onFocusChangeListener
        }
    }
}
