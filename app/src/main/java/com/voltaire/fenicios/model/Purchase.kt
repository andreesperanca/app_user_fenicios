package com.voltaire.fenicios.model

data class Purchase(
    var Price: String = "",
    var Address: Address? = null,
    val Buyer: String = "",
    val Status : String = "",
    val purchasedItems: MutableList<Product> = emptyArray<Product>().toMutableList(),
)
