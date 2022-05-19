package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val street : String = "",
    val number : String = "",
    val district : String = ""
) : Parcelable
