package com.hicham.wcstoreapp.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

expect open class BaseViewModel constructor() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()

    val effects: SharedFlow<Effect>

    fun triggerEffect(effect: Effect)
}

open class Effect

data class ShowSnackbar(val message: String) : Effect()
data class ShowActionSnackbar(val message: String, val actionText: String, val action: () -> Unit) :
    Effect()