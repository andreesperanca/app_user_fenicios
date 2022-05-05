package com.voltaire.fenicios.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem (
    val name : String,
    val price : Double,
    val amount : Int = 1,
    val finalValue : Double = (amount * price)
) : Parcelable
