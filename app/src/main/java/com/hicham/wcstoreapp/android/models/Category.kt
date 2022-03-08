package com.hicham.wcstoreapp.android.models

import com.hicham.wcstoreapp.android.data.db.entities.CategoryEntity

data class Category(
    val id: Long,
    val name: String
)

fun CategoryEntity.toCategory() = Category(
    id = id,
    name = name
)