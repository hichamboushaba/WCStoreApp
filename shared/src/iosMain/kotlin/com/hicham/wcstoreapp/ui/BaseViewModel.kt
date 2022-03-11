package com.hicham.wcstoreapp.ui

import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual open class BaseViewModel: Closeable {
    actual val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    override fun close() {
        onCleared()
    }
}