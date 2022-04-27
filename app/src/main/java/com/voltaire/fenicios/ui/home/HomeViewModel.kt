package com.voltaire.fenicios.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.repositories.HomeRepository

class HomeViewModel constructor(private val repository: HomeRepository) : ViewModel() {

    var listProducts = MutableLiveData<List<Product>>()

    fun getPromotions() : MutableLiveData<List<Product>> {
        listProducts.value = repository.getPromotions()
        return listProducts
    }

    fun getCategories() : List<Category> {
        return repository.getCategories()
    }

    fun getPizzasSalgadas() : MutableLiveData<List<Product>> {
        listProducts.value = repository.getPizzasSalgadas()
        return listProducts
    }
}
