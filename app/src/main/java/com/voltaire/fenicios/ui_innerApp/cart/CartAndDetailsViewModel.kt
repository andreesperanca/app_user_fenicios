package com.voltaire.fenicios.ui_innerApp.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.repositories.CartAndDetailsRepository

class CartAndDetailsViewModel constructor(private val repository: CartAndDetailsRepository) : ViewModel() {

    private val arrayCart = ArrayList<CartItem>()
    var listCart = MutableLiveData<MutableList<CartItem>>()

    init {
        listCart.value = ArrayList()
    }

    fun addItemCartList (cartItem : CartItem){
        arrayCart.add(cartItem)
        listCart.value = arrayCart
    }
}
