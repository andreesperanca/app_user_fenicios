package com.voltaire.fenicios.database

import android.content.Context
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product

interface FirebaseService {

    fun getPromotions(): List<Product> {

        val cartPizzadeQueijo =
            listOf(
            CartItem("Pizza de Queijo (Pequena) ", 25.00, 1),
            CartItem("Pizza de Queijo (Média) ", 30.00, 1),
            CartItem("Pizza de Queijo (Grande) ", 36.00, 1)
        )
        val listPromotions = listOf(
            Product("Pizza de Queijo", "Pizza Salgada",cartPizzadeQueijo, "TAMANHOS:"),
            Product("Pizza de Bolonhesa", "Pizza Salgada",cartPizzadeQueijo, "TAMANHOS:"),
        )
        return listPromotions
    }

    fun getPizzasDoces(): List<Product> {
        val cartPizzadeChocolate =
            listOf(
                CartItem("Pizza de Chocolate (Pequena) ", 25.00, 1),
                CartItem("Pizza de Chocolate (Média) ", 30.00, 1),
                CartItem("Pizza de Chocolate (Grande) ", 36.00, 1)
            )
        val listDoces = listOf(
            Product("Pizza de Chocolate", "Pizzas Doces", cartPizzadeChocolate, "Tamanhos:"),
            Product("Pizza de Chocolate", "Pizzas Doces", cartPizzadeChocolate, "Tamanhos:")
        )
        return listDoces
    }

    fun getPizzasSalgadas(): List<Product> {
        val cartPizzadeQueijo =
            listOf(
                CartItem("Pizza de Queijo (Pequena) ", 25.00, 1),
                CartItem("Pizza de Queijo (Média) ", 30.00, 1),
                CartItem("Pizza de Queijo (Grande) ", 36.00, 1)
            )
        val listSalgadas = listOf(
            Product("Pizza de Queijo", "Pizza Salgada",cartPizzadeQueijo, "TAMANHOS:"),
            Product("Pizza de Bolonhesa", "Pizza Salgada",cartPizzadeQueijo, "TAMANHOS:"),
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