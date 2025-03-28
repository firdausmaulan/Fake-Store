package com.fd.fakestore.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("name")
    val name: Name? = null,
    @SerialName("address")
    val address: Address? = null,
    @SerialName("phone")
    val phone: String? = null,
    @SerialName("token")
    val token: String? = null
)

@Serializable
data class Name(
    @SerialName("firstname")
    val firstname: String? = null,
    @SerialName("lastname")
    val lastname: String? = null 
)

@Serializable
data class Address(
    @SerialName("geolocation")
    val geolocation: Geolocation? = null,
    @SerialName("city")
    val city: String? = null,
    @SerialName("street")
    val street: String? = null,
    @SerialName("number")
    val number: Int? = null,
    @SerialName("zipcode")
    val zipcode: String? = null 
)

@Serializable
data class Geolocation(
    @SerialName("lat")
    val lat: Double? = null,
    @SerialName("long")
    val long: Double? = null
)