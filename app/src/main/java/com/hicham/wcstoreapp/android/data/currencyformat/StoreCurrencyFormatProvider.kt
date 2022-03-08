package com.hicham.wcstoreapp.android.data.currencyformat

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hicham.wcstoreapp.android.data.api.NetworkFormattable
import com.hicham.wcstoreapp.android.data.api.WooCommerceApi
import com.hicham.wcstoreapp.android.data.api.toDomainModel
import com.hicham.wcstoreapp.android.di.AppCoroutineScope
import com.hicham.wcstoreapp.android.models.CurrencyFormatSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

// We are storing an object using Json's serialization, proto-buffers for this simple case seems like
// an overkill
@Singleton
class StoreCurrencyFormatProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val wooCommerceApi: WooCommerceApi,
    @AppCoroutineScope private val coroutineScope: CoroutineScope
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
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed(60000L), replay = 1)

    private fun fetchFormatSettings() {
        coroutineScope.launch {
            try {
                val newSettings = wooCommerceApi.getProducts(1)[0].prices as NetworkFormattable
                dataStore.edit {
                    it[stringPreferencesKey(SETTINGS_KEY)] = json.encodeToString(newSettings)
                }
            } catch (e: Exception) {
                logcat(LogPriority.WARN) {
                    e.asLog()
                }
            }
        }
    }
}