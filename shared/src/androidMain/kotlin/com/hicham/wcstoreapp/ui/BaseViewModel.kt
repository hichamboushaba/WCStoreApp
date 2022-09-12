package com.hicham.wcstoreapp.ui

import androidx.lifecycle.ViewModel
import com.hicham.wcstoreapp.ui.main.MainViewModel
import kotlinx.coroutines.flow.*
import androidx.lifecycle.viewModelScope as archViewModelScope

actual open class BaseViewModel : ViewModel() {
    actual val viewModelScope = archViewModelScope
    actual override fun onCleared() = super.onCleared()

    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = Int.MAX_VALUE)
    actual val effects = _effects.asSharedFlow()

    actual fun triggerEffect(effect: Effect) {
        _effects.tryEmit(effect)
    }

    actual fun <T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}