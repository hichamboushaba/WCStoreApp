package com.hicham.wcstoreapp.data.category.fake

import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeCategoryRepository @Inject constructor() : CategoryRepository {
    override val categories: Flow<List<Category>>
        get() = flowOf(
            listOf(
                Category(0L, "Tshirts"),
                Category(1L, "Hoodies"),
                Category(2L, "Accessories"),
                Category(3L, "Decor")
            )
        )

    override suspend fun refresh() {
        // No op
    }
}