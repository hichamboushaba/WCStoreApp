package com.hicham.wcstoreapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as archViewModelScope

actual open class BaseViewModel : ViewModel() {
    actual val viewModelScope = archViewModelScope
    actual override fun onCleared() = super.onCleared()
}