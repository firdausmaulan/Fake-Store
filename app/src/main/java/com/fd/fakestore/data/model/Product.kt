package com.fd.fakestore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "t_product")
data class Product(
    @SerialName("id")
    @PrimaryKey val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("price")
    val price: Double,
    @SerialName("description")
    val description: String,
    @SerialName("category")
    val category: String,
    @SerialName("image")
    val image: String
)