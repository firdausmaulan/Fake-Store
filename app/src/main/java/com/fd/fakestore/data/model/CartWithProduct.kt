package com.fd.fakestore.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CartWithProduct(
    @Embedded val cart: Cart,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
)