package com.voltaire.fenicios.model

data class User(
    val uid: String = "",
    val name: String = "",
    val numberPhone: String = "",
    val cart: MutableList<Product> = emptyArray<Product>().toMutableList(),
    val address: Address ?= null
)
