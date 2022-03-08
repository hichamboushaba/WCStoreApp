package com.hicham.wcstoreapp.android.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.android.data.api.NetworkCategory
import com.hicham.wcstoreapp.models.Category

@Entity
data class CategoryEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val slug: String
)

fun NetworkCategory.toEntity() = CategoryEntity(
    id = id,
    name = name,
    slug = slug
)

fun CategoryEntity.toDomainModel() = Category(
    id = id,
    name = name
)