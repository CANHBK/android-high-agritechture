package com.example.administrator.glasshouse.SupportClass

import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

class GraphQL {
    fun <D : Operation.Data, T, V : Operation.Variables> query(queryCmd: Query<D, T, V>) {
        var result:T;
        MyApolloClient.getApolloClient().query(queryCmd)
                .enqueue(object : ApolloCall.Callback<T>() {
                    override fun onFailure(e: ApolloException) {
                        Log.d("!test",e.toString())
                    }

                    override fun onResponse(response: Response<T>) {
                        result= response.data()!!
                      Log.d("!test",response.data().toString())
                    }
                })

    }
}