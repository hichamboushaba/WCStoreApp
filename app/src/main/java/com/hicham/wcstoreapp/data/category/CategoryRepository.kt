package com.hicham.wcstoreapp.data.category

import com.hicham.wcstoreapp.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun refresh()
}