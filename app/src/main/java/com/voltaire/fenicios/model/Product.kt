package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id : Long,
    val name : String,
    val category: String,
    val price : Double
) : Parcelable
