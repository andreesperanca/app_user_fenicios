package com.voltaire.fenicios.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

class HomeRepository(private val firebaseService: FirebaseService) {

    suspend fun loadUser (db: FirebaseFirestore, auth : FirebaseAuth) = firebaseService.loadUser(db, auth)

    suspend fun getCategories(db: FirebaseFirestore) = firebaseService.getCategories(db)

}