package com.hicham.wcstoreapp.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hicham.wcstoreapp.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val productId = savedStateHandle.get<Long>(Screen.Product.productIdKey)

    init {
        println("productId -> $productId")
    }
}