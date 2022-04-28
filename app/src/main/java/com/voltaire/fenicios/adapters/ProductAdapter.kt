package com.voltaire.fenicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.voltaire.fenicios.R
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.ui_innerApp.home.HomeFragmentDirections


class ProductAdapter(var productsList : List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productsList[position])

        holder.itemView.setOnClickListener { it: View? ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(productsList[position])
            it?.findNavController()?.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun setProductsAdapter(list : List<Product>) {
        productsList = list
        notifyDataSetChanged()
    }

    inner class ProductViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        val productName : TextView = itemView.findViewById(R.id.productName)

        fun bind(product: Product) {
            productName.text = product.name
        }
    }
}