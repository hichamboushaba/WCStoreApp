package com.hicham.wcstoreapp.android.data.category

import com.hicham.wcstoreapp.android.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val categories: Flow<List<Category>>
    suspend fun refresh()
}