package com.hicham.wcstoreapp.android.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.android.data.api.NetworkProduct
import com.hicham.wcstoreapp.android.models.ProductPrices
import com.hicham.wcstoreapp.android.models.toDomainModel

@Entity
data class ProductEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val images: List<String>,
    @Embedded val prices: ProductPrices,
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