package com.hicham.wcstoreapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.data.api.NetworkProduct

@Entity
data class ProductEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val images: List<String>,
    @ColumnInfo val price: String,
    @ColumnInfo val shortDescription: String,
    @ColumnInfo val description: String
)

fun NetworkProduct.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        images = images.map { it.src },
        price = price.toPlainString(),
        shortDescription = shortDescription,
        description = description
    )
}