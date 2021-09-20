package com.hicham.wcstoreapp.di

import androidx.paging.PagingSource
import com.hicham.wcstoreapp.BuildConfig
import com.hicham.wcstoreapp.data.ProductsRepository
import com.hicham.wcstoreapp.data.ProductsRepositoryImpl
import com.hicham.wcstoreapp.data.source.network.ProductsPagingSource
import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import com.hicham.wcstoreapp.models.Product
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
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
            isLenient = true
        }

        @Provides
        @Singleton
        fun providesRetrofit(json: Json): Retrofit {
            val contentType = MediaType.get("application/json")
            val consumer = OkHttpOAuthConsumer(
                BuildConfig.WC_CONSUMER_KEY,
                BuildConfig.WC_CONSUMER_SECRET
            )

            val httpClient = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))
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