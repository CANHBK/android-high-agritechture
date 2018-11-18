package com.example.administrator.glasshouse.SupportClass

import android.annotation.SuppressLint
import android.os.Parcel
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion

@SuppressLint("ParcelCreator")
class Suggestion : SearchSuggestion {
    var mName: String = ""
    var mIsHistory: String = ""
    constructor(suggestion: String) {
        mName = suggestion.toLowerCase()
    }
    fun getIsHistory(): String {
        return this.mIsHistory
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun getBody(): String {
        return mName
    }

}