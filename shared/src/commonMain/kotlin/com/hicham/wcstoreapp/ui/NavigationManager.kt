package com.hicham.wcstoreapp.ui

import io.ktor.utils.io.core.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface NavigationManager {
    fun navigate(route: String)
    fun navigateUp()
    fun popUpTo(route: String, inclusive: Boolean = false)
    fun <T : Any> navigateBackWithResult(
        key: String,
        result: T,
        destination: String? = null
    )

    fun <T : Any> observeResult(key: String, route: String? = null, onEach: (T) -> Unit): Closeable
}

fun <T : Any> NavigationManager.observeResultAsFlow(key: String, route: String? = null): Flow<T> {
    return callbackFlow {
        val closeable = observeResult<T>(key, route) {
            trySend(it)
        }

        awaitClose { closeable.close() }
    }
}