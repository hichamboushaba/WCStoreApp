package com.hicham.wcstoreapp.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.hicham.wcstoreapp.data.api.NetworkCart
import com.hicham.wcstoreapp.models.CartTotals

@Entity
data class CartEntity(
    @Embedded val totals: CartTotals
) {
    @PrimaryKey
    var id: Int = ID

    companion object {
        const val ID = 0
    }
}


data class CartWithItemsEntity(
    @Embedded val cartEntity: CartEntity,
    @Relation(
        entity = CartItemEntity::class,
        parentColumn = "id",
        entityColumn = "cartId"
    )
    val items: List<CartItemWithProduct>
)

fun NetworkCart.toEntity() = CartEntity(
    totals = with(totals) {
        CartTotals(
            subtotal = calculatePrice(totalItems),
            tax = calculatePrice(totalTax),
            shippingEstimate = totalShipping?.let { calculatePrice(it) },
            total = calculatePrice(totalPrice)
        )
    }
)