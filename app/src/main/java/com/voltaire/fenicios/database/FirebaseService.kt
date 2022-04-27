package com.voltaire.fenicios.database

import android.content.Context
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

interface FirebaseService {

    fun getPromotions(): List<Product> {
        val listPromotions = listOf(
            Product(1, "Pizza de Chocolate", "Pizzas Doces", 25.00),
            Product(2, "Pizza de Frango", "Pizzas Salgadas", 28.00)
        )
        return listPromotions
    }

    fun getPizzasDoces(): List<Product> {
        val listDoces = listOf(
            Product(1, "Pizza de Chocolate", "Pizzas Doces", 25.00),
            Product(2, "Pizza de Beijinho", "Pizzas Doces", 28.00)
        )
        return listDoces
    }

    fun getPizzasSalgadas(): List<Product> {
        val listSalgadas = listOf(
            Product(1, "Pizza de Queijo", "Pizzas Salgadas", 25.00),
            Product(2, "Pizza de Frango", "Pizzas Salgadas", 28.00)
        )
        return listSalgadas
    }

    fun getCategories(): List<Category> {
        val listCategory = listOf(
            Category("Promoções", getPromotions()),
            Category("Pizzas Salgadas", getPizzasSalgadas()),
            Category("Pizzas Doces", getPizzasDoces()),
        )
        return listCategory
    }

    companion object : FirebaseService {
        fun newInstance(): Context {
            val fragment = this
            return fragment.newInstance()
        }
    }
}