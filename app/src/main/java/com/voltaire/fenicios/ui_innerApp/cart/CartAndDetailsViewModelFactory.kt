package com.voltaire.fenicios.ui_innerApp.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voltaire.fenicios.repositories.CartAndDetailsRepository

class CartAndDetailsViewModelFactory constructor(
    private val repository: CartAndDetailsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CartAndDetailsViewModel::class.java)) {
            CartAndDetailsViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}