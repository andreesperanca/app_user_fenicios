package com.voltaire.fenicios.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

class HomeRepository(private val firebaseService: FirebaseService) {

    suspend fun getAllProducts (db: FirebaseFirestore) = firebaseService.allProducts(db)
    suspend fun getCategories(db: FirebaseFirestore) = firebaseService.getCategories(db)

}