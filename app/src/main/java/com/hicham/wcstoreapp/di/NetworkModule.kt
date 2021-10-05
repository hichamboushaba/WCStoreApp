package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.BuildConfig
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import logcat.LogPriority
import logcat.logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModule {
    companion object {
        @Provides
        fun providesJson(): Json = Json {
            // TODO we can probably remove this, and filter the API using the _fields parameter instead for better performance
            ignoreUnknownKeys = true
            isLenient = true
        }

        @Provides
        @Singleton
        fun providesRetrofit(json: Json): Retrofit {
            val contentType = "application/json".toMediaType()
            val consumer = OkHttpOAuthConsumer(
                BuildConfig.WC_CONSUMER_KEY,
                BuildConfig.WC_CONSUMER_SECRET
            )

            val loggingInterceptor = HttpLoggingInterceptor { message ->
                logcat(priority = LogPriority.DEBUG, message = { message })
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.WC_URL)
                .client(httpClient)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
        }

        @Provides
        fun providesApi(retrofit: Retrofit): WooCommerceApi {
            return retrofit.create(WooCommerceApi::class.java)
        }
    }
}