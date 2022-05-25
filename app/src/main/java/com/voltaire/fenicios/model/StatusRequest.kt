package com.voltaire.fenicios.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class StatusRequest : Parcelable {
    PENDENTE,
    PREPARANDO,
    ENTREGANDO
}