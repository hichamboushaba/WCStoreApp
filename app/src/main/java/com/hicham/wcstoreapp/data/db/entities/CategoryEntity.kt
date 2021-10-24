package com.hicham.wcstoreapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.data.api.NetworkCategory

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