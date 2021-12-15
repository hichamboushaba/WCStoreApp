package com.hicham.wcstoreapp.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
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