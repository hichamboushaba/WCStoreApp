package com.hicham.wcstoreapp.di

import com.hicham.wcstoreapp.data.source.network.WooCommerceApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    fun providesJson(): Json = Json

    @Provides
    fun providesRetrofit(): Retrofit {
        val contentType = MediaType.get("application/json")
        return Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    fun providesApi(): WooCommerceApi {
        TODO()
    }
}