package com.hicham.wcstoreapp.data.db.entities

import androidx.room.*

@Entity
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
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