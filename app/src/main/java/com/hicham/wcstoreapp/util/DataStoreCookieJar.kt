package com.hicham.wcstoreapp.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.hicham.wcstoreapp.BuildConfig
import com.hicham.wcstoreapp.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class DataStoreCookieJar(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    @AppCoroutineScope private val coroutineScope: CoroutineScope
) : CookieJar {
    private val cookiesJar = MutableStateFlow<Set<Cookie>?>(null)

    init {
        coroutineScope.launch {
            cookiesJar.value = (dataStore.data.first()[stringSetPreferencesKey("cookies")]
                ?: emptySet()).map { it.decodeCookie() }.toSet()
        }

        cookiesJar
            .filterNotNull()
            .onEach { set ->
                dataStore.edit { prefs ->
                    prefs[stringSetPreferencesKey("cookies")] = set.map {
                        it.encodeCookie()
                    }.toSet()
                }
            }
            .launchIn(coroutineScope)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (!BuildConfig.WC_URL.contains(url.host)) return emptyList()
        return cookiesJar.value?.toList() ?: runBlocking {
            // Wait until the cookies are read from DataStore
            cookiesJar.filterNotNull().first().toList()
        }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (!BuildConfig.WC_URL.contains(url.host)) return
        cookiesJar.update { it.orEmpty() + cookies }
    }

    private fun String.decodeCookie(): Cookie {
        return json.decodeFromString<Map<String, String>>(this).let {
            val builder = Cookie.Builder()
                .name(it["name"]!!)
                .value(it["value"]!!)
                .domain(it["domain"]!!)
                .path(it["path"]!!)
                .expiresAt(it["expiresAt"]!!.toLong())

            if (it["hostOnly"].toBoolean()) {
                builder.hostOnlyDomain(it["domain"]!!)
            }
            if (it["secure"].toBoolean()) {
                builder.secure()
            }
            builder.build()
        }
    }

    private fun Cookie.encodeCookie(): String {
        val values = mapOf(
            "name" to name,
            "value" to value,
            "domain" to domain,
            "hostOnly" to hostOnly.toString(),
            "expiresAt" to expiresAt.toString(),
            "path" to path,
            "secure" to secure.toString(),
            "persistent" to persistent.toString()
        )
        return json.encodeToString(values)
    }
}
