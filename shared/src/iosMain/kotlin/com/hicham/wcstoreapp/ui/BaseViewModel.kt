package com.hicham.wcstoreapp.ui

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

actual open class BaseViewModel : Closeable {
    @NativeCoroutineScope
    actual val viewModelScope = MainScope()

    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = Int.MAX_VALUE)
    actual val effects = _effects.asSharedFlow()

    actual fun triggerEffect(effect: Effect) {
        _effects.tryEmit(effect)
    }

    actual fun <T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = initialValue
    )

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    override fun close() {
        onCleared()
    }
}
