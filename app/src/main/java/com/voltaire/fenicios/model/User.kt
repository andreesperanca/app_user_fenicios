package com.voltaire.fenicios.model

data class User(
    val uid: String = "",
    val name: String = "",
    val numberPhone: String = "",
    val email : String = "",
    val cart: MutableList<Product> = emptyArray<Product>().toMutableList(),
    val requests: MutableList<Purchase> = emptyArray<Purchase>().toMutableList(),
    val address: Address ?= null
)
