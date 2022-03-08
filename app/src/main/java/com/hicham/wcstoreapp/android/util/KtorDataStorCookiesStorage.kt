package com.hicham.wcstoreapp.android.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import io.ktor.client.features.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min

const val COOKIES_STORAGE_KEY = "ktor-cookies"

class KtorDataStorCookiesStorage(
    private val dataStore: DataStore<Preferences>
) : CookiesStorage {
    private val oldestCookie: AtomicLong = AtomicLong(0L)
    private val key = stringSetPreferencesKey(COOKIES_STORAGE_KEY)

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val date = GMTDate()
        if (date.timestamp >= oldestCookie.get()) cleanup(date.timestamp)

        return dataStore.data.first()[key]
            ?.map { parseServerSetCookieHeader(it) }
            ?.filter { it.matches(requestUrl) }
            ?: emptyList()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit {
        dataStore.edit { preferences ->
            val cookies = (preferences[key]
                ?.map { parseServerSetCookieHeader(it) }
                ?: emptyList()).toMutableList()

            cookies.removeAll { it.name == cookie.name && it.matches(requestUrl) }
            cookies.add(cookie.fillDefaults(requestUrl))

            cookie.expires?.timestamp?.let { expires ->
                if (oldestCookie.get() > expires) {
                    oldestCookie.set(expires)
                }
            }

            preferences[key] =
                cookies.map { renderSetCookieHeader(it) }.toSet()
        }
    }

    override fun close() {
    }

    private suspend fun cleanup(timestamp: Long) {
        dataStore.edit { preferences ->
            val cookies = (preferences[key]
                ?.map { parseServerSetCookieHeader(it) }
                ?: emptyList()).toMutableList()

            cookies.removeAll { cookie ->
                val expires = cookie.expires?.timestamp ?: return@removeAll false
                expires < timestamp
            }

            val newOldest = cookies.fold(Long.MAX_VALUE) { acc, cookie ->
                cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
            }

            oldestCookie.set(newOldest)

            preferences[key] =
                cookies.map { renderSetCookieHeader(it) }.toSet()
        }
    }

    private fun Cookie.matches(requestUrl: Url): Boolean {
        val domain = domain?.lowercase(Locale.ROOT)?.trimStart('.')
            ?: error("Domain field should have the default value")

        val path = with(path) {
            val current = path ?: error("Path field should have the default value")
            if (current.endsWith('/')) current else "$path/"
        }

        val host = requestUrl.host.lowercase(Locale.ROOT)
        val requestPath = let {
            val pathInRequest = requestUrl.encodedPath
            if (pathInRequest.endsWith('/')) pathInRequest else "$pathInRequest/"
        }

        if (host != domain && (hostIsIp(host) || !host.endsWith(".$domain"))) {
            return false
        }

        if (path != "/" &&
            requestPath != path &&
            !requestPath.startsWith(path)
        ) return false

        return !(secure && !requestUrl.protocol.isSecure())
    }

    private fun Cookie.fillDefaults(requestUrl: Url): Cookie {
        var result = this

        if (result.path?.startsWith("/") != true) {
            result = result.copy(path = requestUrl.encodedPath)
        }

        if (result.domain.isNullOrBlank()) {
            result = result.copy(domain = requestUrl.host)
        }

        return result
    }
}