package com.voltaire.fenicios.model

data class Purchase(
    var Price : String,
    var Address : Address,
    val Buyer : String,
    val Item : List<Product>,
)
