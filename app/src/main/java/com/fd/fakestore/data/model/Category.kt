package com.fd.fakestore.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("name")
    var name : String?,
    @SerialName("selected")
    var selected : Boolean = false
)