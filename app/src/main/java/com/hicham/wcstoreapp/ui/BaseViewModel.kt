package com.hicham.wcstoreapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

open class BaseViewModel : ViewModel() {
    private val _effects = MutableSharedFlow<Effect>(extraBufferCapacity = Int.MAX_VALUE)
    val effects = _effects.asSharedFlow()

    fun triggerEffect(effect: Effect) {
        _effects.tryEmit(effect)
    }
}

open class Effect

data class ShowSnackbar(val message: String) : Effect()
data class ShowActionSnackbar(val message: String, val actionText: String, val action: () -> Unit) :
    Effect()