package com.hicham.wcstoreapp.ui

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

actual open class BaseViewModel : Closeable {
    @NativeCoroutineScope
    actual val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = Int.MAX_VALUE)
    actual val effects = _effects.asSharedFlow()

    actual fun triggerEffect(effect: Effect) {
        _effects.tryEmit(effect)
    }

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    override fun close() {
        onCleared()
    }
}