package com.hicham.wcstoreapp.util

import io.ktor.client.features.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.atomicfu.AtomicLong
import kotlinx.atomicfu.atomic
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import kotlin.math.min

const val COOKIES_STORAGE_KEY = "ktor-cookies"

class KtorUserDefaultsCookiesStorage() : CookiesStorage {
    private val userDefaults by lazy { NSUserDefaults() }

    private val oldestCookie: AtomicLong = atomic(0L)

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val date = GMTDate()
        if (date.timestamp >= oldestCookie.value) cleanup(date.timestamp)

        val cookies = userDefaults.stringForKey(COOKIES_STORAGE_KEY)
            ?.takeIf { it.isNotBlank() }
            ?.split("|")
            ?.map { parseServerSetCookieHeader(it) }
            ?.filter { it.matches(requestUrl) }
            ?: emptyList()

		return cookies
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit {
        val cookies = (userDefaults.stringForKey(COOKIES_STORAGE_KEY)
            ?.takeIf { it.isNotBlank() }
            ?.split("|")
            ?.map { parseServerSetCookieHeader(it) }
            ?: emptyList()).toMutableList()

        cookies.removeAll { it.name == cookie.name && it.matches(requestUrl) }
        cookies.add(cookie.fillDefaults(requestUrl))

        cookie.expires?.timestamp?.let { expires ->
            if (oldestCookie.value > expires) {
                oldestCookie.lazySet(expires)
            }
        }

        userDefaults.setValue(
            cookies.joinToString("|") { renderSetCookieHeader(it) },
            forKey = COOKIES_STORAGE_KEY
        )
    }

    private fun cleanup(timestamp: Long) {
        val cookies = (userDefaults.stringForKey(COOKIES_STORAGE_KEY)
            ?.takeIf { it.isNotBlank() }
            ?.split("|")
            ?.map { parseServerSetCookieHeader(it) }
            ?: emptyList()).toMutableList()

        cookies.removeAll { cookie ->
            val expires = cookie.expires?.timestamp ?: return@removeAll false
            expires < timestamp
        }

        val newOldest = cookies.fold(Long.MAX_VALUE) { acc, cookie ->
            cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
        }

        oldestCookie.lazySet(newOldest)

        userDefaults.setValue(
            cookies.joinToString("|") { renderSetCookieHeader(it) },
            forKey = COOKIES_STORAGE_KEY
        )
    }

    private fun Cookie.matches(requestUrl: Url): Boolean {
        val domain = domain?.lowercase()?.trimStart('.')
            ?: error("Domain field should have the default value")

        val path = with(path) {
            val current = path ?: error("Path field should have the default value")
            if (current.endsWith('/')) current else "$path/"
        }

        val host = requestUrl.host.lowercase()
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

    override fun close() {

    }
}
