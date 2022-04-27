package com.voltaire.fenicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.voltaire.fenicios.R
import com.voltaire.fenicios.model.Category

class CategoriesAdapter(private val categoryList : List<Category>,
                        private val listener : CategoriesAdapterCallBacks
                          ) : RecyclerView.Adapter<CategoriesAdapter.TypeProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeProductsViewHolder {
       return TypeProductsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.type_product_item, parent, false
       ),listener)
    }

    override fun onBindViewHolder(holder: TypeProductsViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class TypeProductsViewHolder (itemView : View,
                                        listener: CategoriesAdapterCallBacks
                                        ) : RecyclerView.ViewHolder(itemView) {

        val titleMenu : TextView = itemView.findViewById(R.id.titleMenu)

        fun bind(category: Category) {
            titleMenu.text = category.name

            itemView.setOnClickListener {
                listener.searchClickListener(categoryList[adapterPosition])
            }
        }
    }
}