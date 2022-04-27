package com.voltaire.fenicios.adapters

import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

interface CategoriesAdapterCallBacks {

    fun searchClickListener(category : Category)
}