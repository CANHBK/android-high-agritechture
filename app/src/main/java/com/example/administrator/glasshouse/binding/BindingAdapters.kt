package com.example.administrator.glasshouse.binding

import androidx.databinding.BindingAdapter
import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher



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
