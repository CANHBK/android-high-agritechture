package com.example.administrator.glasshouse.api

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query

interface GraphQL {
    fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>, apolloCallback: ApolloCall.Callback<T>)
}
