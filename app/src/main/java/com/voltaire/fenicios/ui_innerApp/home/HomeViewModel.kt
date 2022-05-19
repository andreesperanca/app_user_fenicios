package com.voltaire.fenicios.ui_innerApp.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.model.User
import com.voltaire.fenicios.repositories.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val repository: HomeRepository) : ViewModel() {


    var listProducts = MutableLiveData<List<Product>>()
    var listCategories = MutableLiveData<List<Category>>()
    lateinit var cUser : User

    fun getCategories (db: FirebaseFirestore) {
        viewModelScope.launch {
                listCategories.value = repository.getCategories(db)
                listProducts.value = repository.getCategories(db)[0].listProduct
        }
    }

    fun loadUser (auth : FirebaseAuth, db : FirebaseFirestore) =
        viewModelScope.launch {
            cUser = repository.loadUser(db, auth).toObject(User::class.java)!!
        }
}
