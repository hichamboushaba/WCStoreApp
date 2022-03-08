package com.hicham.wcstoreapp.android.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val NONCE_KEY = "last_nonce"
private const val NONCE_HEADER = "X-WC-Store-API-Nonce"

class NonceKtorPlugin(
    private val dataStore: DataStore<Preferences>
) : HttpClientFeature<Unit, Unit> {
    override val key: AttributeKey<Unit> = AttributeKey("NonceKtorPlugin")

    override fun install(feature: Unit, scope: HttpClient) {
        scope.sendPipeline.intercept(HttpSendPipeline.Before) {
            val lastNonce = runBlocking {
                dataStore.data.first()[stringPreferencesKey(NONCE_KEY)]
            }

            if (!lastNonce.isNullOrEmpty()) {
                context.header(NONCE_HEADER, lastNonce)
            }

            proceed()
        }

        scope.receivePipeline.intercept(HttpReceivePipeline.After) {
            context.response.headers[NONCE_HEADER]?.let { nonce ->
                runBlocking {
                    dataStore.edit {
                        it[stringPreferencesKey(NONCE_KEY)] = nonce
                    }
                }
            }

            proceed()
        }
    }

    override fun prepare(block: Unit.() -> Unit) = Unit
}