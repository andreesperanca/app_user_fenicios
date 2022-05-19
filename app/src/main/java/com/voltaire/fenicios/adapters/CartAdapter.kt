package com.voltaire.fenicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.CartPruductItemBinding
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.model.Product
import kotlinx.coroutines.processNextEventInCurrentThread

class CartAdapter(private val productsList: List<Product> = emptyList(),
                  private val listener : CartAdapterCallBacks
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartPruductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    inner class CartViewHolder(private val binding : CartPruductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.cartNameProduct.text = binding.root.context.getString(R.string.cartProductName, product.name)
            binding.valorProduct.text = product.finalPrice.toString()
            binding.valorProduct.text = binding.root.context.getString(R.string.cartPrice, product.finalPrice.toString())
            binding.qtdProduct.text = binding.root.context.getString(R.string.cartQtd, product.qtd.toString())
            binding.sizeProduct.text = binding.root.context.getString(R.string.sizeProduct,product.size)

            binding.excludeItem.setOnClickListener {
                listener.ExcludeItemListener(productsList[position])
            }

            Glide.with(binding.root.context)
                .load(product.url)
                .into(binding.imageCartProduct)
        }
    }
}