package com.example.administrator.glasshouse.api
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import javax.inject.Inject

class Apollo @Inject constructor(val apolloClient: ApolloClient) : GraphQL {
    override fun <D : Operation.Data, T, V : Operation.Variables> query(query: Query<D, T, V>, apolloCallback: ApolloCall.Callback<T>) {
        apolloClient.query(query)
                .enqueue(apolloCallback)
    }
}