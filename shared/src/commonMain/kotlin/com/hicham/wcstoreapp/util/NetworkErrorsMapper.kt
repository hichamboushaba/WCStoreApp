package com.hicham.wcstoreapp.util

import io.ktor.client.features.*
import io.ktor.utils.io.errors.*

suspend fun <T> Any.runCatchingNetworkErrors(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: ResponseException) {
        log(LogPriority.WARN) { e.stackTraceToString() }
        Result.failure(e)
    } catch (e: KtorNetworkException) {
        log(LogPriority.WARN) { e.stackTraceToString() }
        Result.failure(e)
    } catch (e: IOException) {
        log(LogPriority.WARN) { e.stackTraceToString() }
        Result.failure(e)
    }
}

class KtorNetworkException(cause: Throwable) : Exception(cause)