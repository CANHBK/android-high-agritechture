package com.example.administrator.glasshouse.Adapter

import android.view.View

interface ItemClickedListener {
    fun onClick(view: View, position: Int, isLongClick: Boolean)
}