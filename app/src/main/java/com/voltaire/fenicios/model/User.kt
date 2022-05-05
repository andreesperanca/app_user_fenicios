package com.voltaire.fenicios.models

data class User(
    val uid: String = "",
    val name: String = "",
    val numberPhone: String = "",
    val cart : List<CartItem> = emptyList(),
    val address : Address ?= null
)