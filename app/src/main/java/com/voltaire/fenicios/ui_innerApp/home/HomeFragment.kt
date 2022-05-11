package com.voltaire.fenicios.ui_innerApp.home

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.adapters.CategoriesAdapterCallBacks
import com.voltaire.fenicios.adapters.ProductAdapter
import com.voltaire.fenicios.adapters.CategoriesAdapter
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.databinding.FragmentHomeBinding
import com.voltaire.fenicios.model.Category
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.repositories.HomeRepository
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeFragment : Fragment(), CategoriesAdapterCallBacks {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewProductType: RecyclerView
    private lateinit var productTypeAdapter: CategoriesAdapter

    private lateinit var recyclerViewProduct: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewmodel: HomeViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore



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
            )
        ).get(HomeViewModel::class.java)

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth


        viewmodel.getCategories(db)

    }

    override fun onStart() {
        super.onStart()

    recyclerViewProduct = binding.rvProduct
        productAdapter = ProductAdapter(emptyList())
        recyclerViewProduct.adapter = productAdapter
        recyclerViewProduct.layoutManager = GridLayoutManager(requireContext(), 2)

        recyclerViewProductType = binding.rvProductType
        productTypeAdapter = CategoriesAdapter(emptyList(), this)
        recyclerViewProductType.adapter = productTypeAdapter
        recyclerViewProductType.layoutManager =
            LinearLayoutManager(requireContext(), HORIZONTAL, false)

    }

    override fun onResume() {
        super.onResume()

        viewmodel.listProducts.observe(viewLifecycleOwner) { produtsList ->
            productAdapter.setProductsAdapter(produtsList)
        }

        viewmodel.listCategories.observe(viewLifecycleOwner) { categoriesList ->
            productTypeAdapter.setCategories(categoriesList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel.listProducts.value = emptyList()
        viewmodel.listCategories.value = emptyList()
    }

    override fun searchClickListener(category: Category) {
        viewmodel.listProducts.value = category.listProduct
    }

    private suspend fun loadCategories() {
        val list = mutableListOf<Category>()

        val requestUsers =
            db.collection("produtos")
                .get()
                .await()
                .documents
                .map {
                    it.toObject(Category::class.java)
                }
        Log.i("teste", requestUsers.size.toString())
    }
}
