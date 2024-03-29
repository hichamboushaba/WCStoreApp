package com.hicham.wcstoreapp.data.currencyformat

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hicham.wcstoreapp.data.storeApi.NetworkFormattable
import com.hicham.wcstoreapp.data.storeApi.WooCommerceApi
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.hicham.wcstoreapp.util.LogPriority
import com.hicham.wcstoreapp.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StoreCurrencyFormatProvider(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val wooCommerceApi: WooCommerceApi
) : CurrencyFormatProvider {
    companion object {
        const val SETTINGS_KEY = "currency_format_settings"
    }

    override val formatSettings: Flow<CurrencyFormatSettings> = dataStore
        .data
        .map { preferences ->
            preferences[stringPreferencesKey(SETTINGS_KEY)]?.let {
                json.decodeFromString<NetworkFormattable>(it).toDomainModel()
            } ?: CurrencyFormatSettings()
        }
        .distinctUntilChanged()
        .onStart {
            // Refresh the format settings when the flow is restarted
            fetchFormatSettings()
        }
        .shareIn(
            coroutineScope + Dispatchers.Main,
            SharingStarted.WhileSubscribed(60000L),
            replay = 1
        )

    private fun fetchFormatSettings() {
        coroutineScope.launch {
            try {
                val newSettings = wooCommerceApi.getProducts(1)[0].prices as NetworkFormattable
                dataStore.edit {
                    it[stringPreferencesKey(SETTINGS_KEY)] = json.encodeToString(newSettings)
                }
            } catch (e: Exception) {
                log(priority = LogPriority.WARN) {
                    e.stackTraceToString()
                }
            }
        }
    }
}