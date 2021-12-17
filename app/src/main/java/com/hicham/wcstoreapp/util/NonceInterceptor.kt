package com.hicham.wcstoreapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

private const val NONCE_KEY = "last_nonce"
private const val NONCE_HEADER = "X-WC-Store-API-Nonce"

class NonceInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val lastNonce = runBlocking {
            dataStore.data.first()[stringPreferencesKey(NONCE_KEY)]
        }

        val requestBuilder = chain.request().newBuilder()

        if (!lastNonce.isNullOrEmpty()) {
            requestBuilder.addHeader(NONCE_HEADER, lastNonce)
        }

        val response = chain.proceed(requestBuilder.build())

        response.header(NONCE_HEADER, null)?.let { nonce ->
            runBlocking {
                dataStore.edit {
                    it[stringPreferencesKey(NONCE_KEY)] = nonce
                }
            }
        }

        return response
    }
}