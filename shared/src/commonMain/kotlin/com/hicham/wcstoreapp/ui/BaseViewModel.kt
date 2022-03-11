package com.hicham.wcstoreapp.ui

import kotlinx.coroutines.CoroutineScope

expect open class BaseViewModel {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}