package com.voltaire.fenicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.voltaire.fenicios.R
import com.voltaire.fenicios.model.CartItem

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    val productsList = emptyList<CartItem>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_pruduct_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productName : TextView = itemView.findViewById(R.id.cartNameProduct)
        val valorProduct : TextView = itemView.findViewById(R.id.valorProduct)
        val imageProduct : ImageView = itemView.findViewById(R.id.imageCartProduct)

        fun bind(product: CartItem) {
            productName.text = product.name
            imageProduct.setImageResource(R.drawable.mock_product)
        }
    }
}