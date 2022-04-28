package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val name : String,
    val category: String,
    val list : List<CartItem>,
    val description : String
) : Parcelable
