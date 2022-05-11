package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  Product(
    val name: String = "",
    val prices: Map<String, String>? = null,
    val category: String = "",
    val description: String = "",
    val url: String = ""
) : Parcelable
