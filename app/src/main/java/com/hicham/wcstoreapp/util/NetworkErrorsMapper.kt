package com.hicham.wcstoreapp.util

import logcat.LogPriority
import logcat.asLog
import logcat.logcat
import okio.IOException
import retrofit2.HttpException

suspend fun <T> Any.runCatchingNetworkErrors(block: suspend () -> T): Result<T> {
    return try {
        val result = block()
        Result.success(result)
    } catch (e: IOException) {
        logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    } catch (e: HttpException) {
        logcat(LogPriority.WARN) { e.asLog() }
        Result.failure(e)
    }
}