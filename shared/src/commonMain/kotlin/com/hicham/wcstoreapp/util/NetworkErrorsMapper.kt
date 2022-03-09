package com.hicham.wcstoreapp.util

import io.ktor.client.features.*

suspend fun <T> runCatchingNetworkErrors(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: ResponseException) {
        //logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    } catch (e: KtorNetworkException) {
        //logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    }
}

class KtorNetworkException(cause: Throwable) : Exception(cause)