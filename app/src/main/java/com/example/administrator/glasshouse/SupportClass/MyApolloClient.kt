package com.example.administrator.glasshouse.SupportClass

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.rx2.Rx2Apollo
//import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
//import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class MyApolloClient {
    companion object {
        private var apolloClient: ApolloClient? = null
        private val BASE_URl_GRAPHQL = "https://high-tech-agriculture.herokuapp.com/graphql"
//        private val BASE_URl_GRAPHQL = "http://10.0.2.2:5000/graphql"
        private val BASE_URl_SUBSCIPTION = "ws://high-tech-agriculture.herokuapp.com/graphql"
//        private val BASE_URl_SUBSCIPTION = "ws://10.0.2.2:5000/graphql"
        private val SQL_CACHE_NAME = "GlassHouse"
//        private val subscriptionSubscriptionClient = GetTempSubcription.builder().build()
//        val observer = Rx2Apollo.from(getSubscriptionSubscriptionCall())
//        observer.subscribeWith(SubscriptionSubscriber ())
//        fun getSubscriptionSubscriptionClient(): GetTempSubcription {
//            return subscriptionSubscriptionClient
//        }
//
//        private fun getSubscriptionSubscriptionCall(): ApolloSubscriptionCall<GetTempSubcription.Data> {
//            return apolloClient!!.subscribe(subscriptionSubscriptionClient)
//        }


        fun getApolloClient(): ApolloClient {

            // Logging để thuận lợi cho việc xem Log server trả về
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .pingInterval(50, TimeUnit.SECONDS)
                    .connectTimeout(30,TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            apolloClient = ApolloClient.builder()
                    .okHttpClient(okHttpClient)
                    .serverUrl(BASE_URl_GRAPHQL)
                    .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory(BASE_URl_SUBSCIPTION, okHttpClient))
                    .build()
            return apolloClient!!
        }
    }
}