package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.db.entities.CategoryEntity

data class Category(
    val id: Long,
    val name: String
)

fun CategoryEntity.toCategory() = Category(
    id = id,
    name = name
)