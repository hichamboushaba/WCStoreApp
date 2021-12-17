package com.hicham.wcstoreapp.data.db.entities

import androidx.room.*
import com.hicham.wcstoreapp.data.api.NetworkCartItem
import com.hicham.wcstoreapp.models.CartItemTotals

@Entity
data class CartItemEntity(
    @PrimaryKey val key: String,
    val quantity: Int,
    @ColumnInfo(index = true) val productId: Long,
    @Embedded val totals: CartItemTotals
) {
    // This is not useful from a business perspective, it's just here for Room's relations
    var cartId = CartEntity.ID
}


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
    productId = id,
    totals = with(totals) {
        CartItemTotals(
            subtotal = calculatePrice(lineSubtotal),
            tax = calculatePrice(lineSubtotalTax),
            total = calculatePrice(lineTotal)
        )
    }
)