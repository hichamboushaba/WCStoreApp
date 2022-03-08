package com.hicham.wcstoreapp.data.category.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.db.entities.toEntity
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.toCategory
import com.hicham.wcstoreapp.util.runCatchingNetworkErrors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DBCategoryRepository @Inject constructor(
    private val database: AppDatabase,
    private val api: WooCommerceApi
) : CategoryRepository {
    private val categoryDao = database.categoryDao()

    override val categories: Flow<List<Category>>
        get() = categoryDao.getCategories()
            .map { list -> list.map { it.toCategory() } }

    override suspend fun refresh() {
        runCatchingNetworkErrors {
            val categories = api.getCategories().map { it.toEntity() }
            database.withTransaction {
                categoryDao.deleteAll()
                categoryDao.insert(*categories.toTypedArray())
            }
        }
    }
}