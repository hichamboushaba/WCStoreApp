package com.hicham.wcstoreapp.data.source.db.entities

import androidx.room.*
import com.hicham.wcstoreapp.models.CartItem

@Entity(
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.NO_ACTION
    )]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val quantity: Int,
    @ColumnInfo(index = true) val productId: Long
)
