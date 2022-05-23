package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  Product(
    val name: String = "",
    val prices: Map<String, String>? = null,
    val category: String = "",
    val description: String = "",
    val url: String = "",
    val qtd: Int? = 1,
    val finalPrice: Float = 0.00f,
    val size: String = "nDefinida"
) : Parcelable
