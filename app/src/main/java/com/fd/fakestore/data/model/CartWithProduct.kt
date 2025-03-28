package com.fd.fakestore.data.model

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

data class CartWithProduct(
    @Embedded val cart: Cart,
    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product,
    @Ignore
    val totalPrice: Double,
    @Ignore
    val isSelected: Boolean
) {
    constructor(cart: Cart, product: Product) : this(
        cart,
        product,
        cart.quantity * product.price,
        false
    )
}