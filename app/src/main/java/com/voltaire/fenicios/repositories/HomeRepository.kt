package com.voltaire.fenicios.repositories

import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

class HomeRepository(private val firebaseService: FirebaseService) {

    fun getPromotions() : List<Product> {
       return firebaseService.getPromotions()
    }

    fun getCategories () : List<Category> {
        return firebaseService.getCategories()
    }

    fun getPizzasSalgadas() : List<Product> {
        return firebaseService.getPizzasSalgadas()
    }

}