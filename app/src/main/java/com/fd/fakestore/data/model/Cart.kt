package com.fd.fakestore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "t_cart")
data class Cart(
    @SerialName("cartId")
    @PrimaryKey(autoGenerate = true) val cartId: Int = 0,
    @SerialName("productId")
    val productId: Int,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("userId")
    val userId: Int
)