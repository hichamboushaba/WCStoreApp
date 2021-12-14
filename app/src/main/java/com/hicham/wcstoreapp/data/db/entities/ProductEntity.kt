package com.hicham.wcstoreapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.models.Prices
import com.hicham.wcstoreapp.models.toDomainModel

@Entity
data class ProductEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val images: List<String>,
    @Embedded val prices: Prices,
    @ColumnInfo val shortDescription: String,
    @ColumnInfo val description: String
)

fun NetworkProduct.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        images = images.map { it.src },
        prices = prices.toDomainModel(),
        shortDescription = shortDescription,
        description = description
    )
}