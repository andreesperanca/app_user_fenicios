package com.voltaire.fenicios.ui_innerApp.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.repositories.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val repository: HomeRepository) : ViewModel() {

    var listProducts = MutableLiveData<List<Product>>()
    var listCategories = MutableLiveData<List<Category>>()



    fun getAllProducts (db: FirebaseFirestore) {
        viewModelScope.launch {
            listProducts.value = repository.getAllProducts(db)
        }
    }
    fun getCategories (db: FirebaseFirestore) {
        viewModelScope.launch {
                listCategories.value = repository.getCategories(db)
                listProducts.value = repository.getCategories(db)[0].listProduct
        }
    }
}
