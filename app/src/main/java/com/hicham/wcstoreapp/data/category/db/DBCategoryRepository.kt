package com.hicham.wcstoreapp.data.category.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.data.api.WooCommerceApi
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.data.db.AppDatabase
import com.hicham.wcstoreapp.data.db.entities.toEntity
import com.hicham.wcstoreapp.models.Category
import com.hicham.wcstoreapp.models.toCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import logcat.LogPriority
import logcat.asLog
import logcat.logcat
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
        try {
            val categories = api.getCategories().map { it.toEntity() }
            database.withTransaction {
                categoryDao.deleteAll()
                categoryDao.insert(*categories.toTypedArray())
            }
        } catch (e: Exception) {
            logcat(LogPriority.ERROR) {
                e.asLog()
            }
        }
    }
}