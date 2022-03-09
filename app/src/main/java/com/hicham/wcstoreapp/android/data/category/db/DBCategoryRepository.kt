package com.hicham.wcstoreapp.android.data.category.db

import androidx.room.withTransaction
import com.hicham.wcstoreapp.android.data.api.WooCommerceApi
import com.hicham.wcstoreapp.android.data.db.AppDatabase
import com.hicham.wcstoreapp.android.data.db.entities.toDomainModel
import com.hicham.wcstoreapp.android.data.db.entities.toEntity
import com.hicham.wcstoreapp.android.util.runCatchingNetworkErrors
import com.hicham.wcstoreapp.data.category.CategoryRepository
import com.hicham.wcstoreapp.models.Category
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
            .map { list -> list.map { it.toDomainModel() } }

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