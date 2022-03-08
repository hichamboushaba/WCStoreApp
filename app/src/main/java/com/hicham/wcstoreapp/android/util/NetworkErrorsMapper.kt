package com.hicham.wcstoreapp.android.util

import io.ktor.client.features.*
import logcat.LogPriority
import logcat.asLog
import logcat.logcat

suspend fun <T> Any.runCatchingNetworkErrors(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: ResponseException) {
        logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    } catch (e: KtorNetworkException) {
        logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    }
}

class KtorNetworkException(cause: Throwable): Exception(cause)