package com.example.administrator.glasshouse.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import com.example.administrator.glasshouse.api.GraphQL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import com.example.administrator.glasshouse.api.Apollo
import java.io.File


@Module(includes = [OkHttpClientModule::class])
class  ApolloModule {
    companion object {
        private val BASE_URl_GRAPHQL = "https://high-tech-agriculture.herokuapp.com/graphql"
        //                private val BASE_URl_GRAPHQL = "http://10.0.2.2:5000/graphql"
        private val BASE_URl_SUBSCIPTION = "ws://high-tech-agriculture.herokuapp.com/graphql"
//        private val BASE_URl_SUBSCIPTION = "ws://10.0.2.2:5000/graphql"
    }


    @Provides
    @Singleton
    fun cacheStore(file: File): DiskLruHttpCacheStore {
        //Size in bytes of the cache
        val size: Long = 1024 * 1024 * 1024

        //Create the http response cache store
        val cacheStore = DiskLruHttpCacheStore(file, size)
        return cacheStore
    }


    @Provides
    @Singleton
    fun apollo(okHttpClient: OkHttpClient, cacheStore: DiskLruHttpCacheStore): ApolloClient {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(BASE_URl_GRAPHQL)
                .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory(BASE_URl_SUBSCIPTION, okHttpClient))
                .build()
    }

    @Provides
    @Singleton
     fun graphQL(apolloClient: ApolloClient): GraphQL{
      return Apollo(apolloClient)
    }
}