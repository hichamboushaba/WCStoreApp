package com.hicham.wcstoreapp.data.db.entities

import androidx.room.*
import com.hicham.wcstoreapp.data.api.NetworkCartItem

@Entity
data class CartItemEntity(
    @PrimaryKey val key: String,
    val quantity: Int,
    @ColumnInfo(index = true) val productId: Long
)


data class CartItemWithProduct(
    @Embedded
    val cartItem: CartItemEntity,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: ProductEntity?
)

fun NetworkCartItem.toEntity() = CartItemEntity(
    key = key,
    quantity = quantity,
    productId = id
)