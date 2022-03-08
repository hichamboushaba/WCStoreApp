package com.hicham.wcstoreapp.android.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.hicham.wcstoreapp.models.CartTotals

@Entity
data class CartEntity(
    @Embedded val totals: CartTotals,
    val primaryShippingAddress: Long?,
    val primaryBillingAddress: Long?
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