package com.voltaire.fenicios.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.RequestsProductItemBinding
import com.voltaire.fenicios.model.Purchase

class RequestsAdapter(private var purchaseList: List<Purchase>) :
    RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val binding =
            RequestsProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        holder.bind(purchaseList[position])
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }

    inner class RequestsViewHolder(private val binding: RequestsProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(purchase: Purchase) {
            binding.priceRequest.text =
                itemView.context.getString(R.string.purchaseValueRequest, purchase.price)
            binding.statusRequest.text =
                itemView.context.getString(R.string.statusRequest, purchase.status.toString())
            binding.AddressRequest.text = itemView.context.getString(
                R.string.addressRequest,
                "${purchase.address?.district},${purchase.address?.street},${purchase.address?.number}"
            )
        }
    }
}