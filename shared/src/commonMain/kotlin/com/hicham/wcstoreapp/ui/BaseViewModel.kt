package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

expect open class BaseViewModel constructor() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()

    val effects: SharedFlow<Effect>

    fun triggerEffect(effect: Effect)

    fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T>
}

open class Effect

data class ShowSnackbar(val message: String) : Effect()
data class ShowActionSnackbar(val message: String, val actionText: String, val action: () -> Unit) :
    Effect()