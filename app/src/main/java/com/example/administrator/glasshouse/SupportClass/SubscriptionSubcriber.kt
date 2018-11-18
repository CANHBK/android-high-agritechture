package com.example.administrator.glasshouse.SupportClass

import android.util.Log
import com.apollographql.apollo.api.Response
import com.example.administrator.glasshouse.TempSubscription
import io.reactivex.subscribers.DisposableSubscriber

abstract class SubscriptionSubcriber : DisposableSubscriber<Response<TempSubscription.Data>>() {
    override fun onComplete() {
        Log.d("!temp","onComplete")
    }

    override fun onNext(response: Response<TempSubscription.Data>?) {
        Log.d("!temp","onNext")
        Log.d("!temp",response!!.data()!!.tempAdded()!!.value().toString())
    }

    override fun onError(t: Throwable?) {
        Log.d("!temp",t!!.message)
    }
}