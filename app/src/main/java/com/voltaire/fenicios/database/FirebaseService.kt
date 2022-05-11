package com.voltaire.fenicios.database

import android.content.ContentValues.TAG
import android.content.Context
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product
import kotlinx.coroutines.tasks.await

interface FirebaseService {

    suspend fun allProducts(db : FirebaseFirestore) : List<Product> {
        val requestProducts =
            db.collection("promoções")
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.toObject(Product::class.java)
                }
        return requestProducts
    }

     suspend fun getCategories(db : FirebaseFirestore) : List<Category> {
        val requestCategories =
            db.collection("produtos")
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.toObject(Category::class.java)
                }
        return requestCategories
    }

    companion object : FirebaseService {
        fun newInstance(): Context {
            val fragment = this
            return fragment.newInstance()
        }
    }
}