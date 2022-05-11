package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val name : String = "",
    val listProduct : List<Product> = emptyList()
) : Parcelable
