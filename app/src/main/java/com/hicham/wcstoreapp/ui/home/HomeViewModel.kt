package com.hicham.wcstoreapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.hicham.wcstoreapp.data.Status
import com.hicham.wcstoreapp.data.source.ProductsRepository
import com.hicham.wcstoreapp.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductsRepository) : ViewModel() {
    val products = repository.getProductList().cachedIn(viewModelScope)
}