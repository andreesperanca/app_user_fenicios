package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Purchase(
    var price: String = "",
    var address: Address? = null,
    val payer: String = "",
    val purchasedItems: MutableList<Product> = emptyArray<Product>().toMutableList(),
    val statusPayment : String = "",
    var status : StatusRequest = StatusRequest.PENDENTE
) : Parcelable
