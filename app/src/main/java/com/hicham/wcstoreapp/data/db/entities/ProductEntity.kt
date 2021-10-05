package com.hicham.wcstoreapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.models.Product
import java.math.BigDecimal

@Entity
data class ProductEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val images: List<String>,
    @ColumnInfo val price: String,
    @ColumnInfo val shortDescription: String,
    @ColumnInfo val description: String
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            images = images,
            price = BigDecimal(price),
            shortDescription = shortDescription,
            description = description
        )
    }
}