package com.hicham.wcstoreapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.hicham.wcstoreapp.BuildConfig
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.util.DataStoreCookieJar
import com.hicham.wcstoreapp.util.NonceInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import logcat.LogPriority
import logcat.logcat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
        fun providesRetrofit(
            json: Json,
            dataStore: DataStore<Preferences>,
            @AppCoroutineScope coroutineScope: CoroutineScope
        ): Retrofit {
            val contentType = "application/json".toMediaType()

            val loggingInterceptor = HttpLoggingInterceptor { message ->
                logcat(priority = LogPriority.DEBUG, message = { message })
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val httpClient = OkHttpClient.Builder()
                .cookieJar(DataStoreCookieJar(dataStore, json, coroutineScope))
                .addInterceptor(NonceInterceptor(dataStore))
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