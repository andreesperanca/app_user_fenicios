package com.voltaire.fenicios.adapters

import android.widget.AdapterView
import com.voltaire.fenicios.model.Product

interface CartAdapterCallBacks {

    fun ExcludeItemListener (product: Product) {}
}