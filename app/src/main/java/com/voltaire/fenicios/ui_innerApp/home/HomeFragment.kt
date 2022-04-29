package com.voltaire.fenicios.ui_innerApp.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.adapters.CategoriesAdapterCallBacks
import com.voltaire.fenicios.adapters.ProductAdapter
import com.voltaire.fenicios.adapters.CategoriesAdapter
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.databinding.FragmentHomeBinding
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.repositories.HomeRepository


class HomeFragment : Fragment(), CategoriesAdapterCallBacks {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var recyclerViewProductType : RecyclerView
    private lateinit var productTypeAdapter : CategoriesAdapter

    private lateinit var recyclerViewProduct : RecyclerView
    private lateinit var productAdapter : ProductAdapter
    private lateinit var viewmodel : HomeViewModel

    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProvider(
            viewModelStore,
            HomeViewModelFactory(
                HomeRepository(FirebaseService)
        )).get(HomeViewModel::class.java)

        viewmodel.getPromotions()
        var listProducts = viewmodel.listProducts.value
        val listCategories = viewmodel.getCategories()

        viewmodel.listProducts.observe(viewLifecycleOwner) { produtsList ->
            productAdapter.setProductsAdapter(produtsList)
        }

        //TITLE HORIZONTAL LIST
        recyclerViewProductType = binding.rvProductType
        productTypeAdapter = CategoriesAdapter(listCategories,this)
        recyclerViewProductType.adapter = productTypeAdapter
        recyclerViewProductType.layoutManager = LinearLayoutManager(requireContext(),HORIZONTAL,false)

        //PRODUCT LIST
        recyclerViewProduct = binding.rvProduct
        productAdapter = ProductAdapter(listProducts!!)
        recyclerViewProduct.adapter = productAdapter
        recyclerViewProduct.layoutManager = GridLayoutManager(requireContext(), 2)

    }

    override fun onStart() {
        super.onStart()

    }

    override fun searchClickListener(category: Category) {
        viewmodel.listProducts.value = category.listProduct
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = Firebase.auth
    }
}