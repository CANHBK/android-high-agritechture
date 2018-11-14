package com.example.administrator.glasshouse

import android.util.Log
import com.apollographql.apollo.api.Response
import io.reactivex.subscribers.DisposableSubscriber

abstract class SubscriptionSubcriber : DisposableSubscriber<Response<TempSubcriptionSubscription.Data>>() {
    override fun onComplete() {
        Log.d("!temp","onComplete")
    }

    override fun onNext(response: Response<TempSubcriptionSubscription.Data>?) {
        Log.d("!temp","onNext")
        Log.d("!temp",response!!.data()!!.tempAdded()!![10].value().toString())
    }

    override fun onError(t: Throwable?) {

    }
}