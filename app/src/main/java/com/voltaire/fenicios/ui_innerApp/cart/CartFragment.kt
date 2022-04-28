package com.voltaire.fenicios.ui_innerApp.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.voltaire.fenicios.adapters.CartAdapter
import com.voltaire.fenicios.databinding.FragmentCartBinding
import com.voltaire.fenicios.repositories.CartAndDetailsRepository


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var viewModel: CartAndDetailsViewModel

    private lateinit var recyclerViewCart : RecyclerView

    private lateinit var cartAdapter : CartAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            viewModelStore,
            CartAndDetailsViewModelFactory(
                CartAndDetailsRepository()
            )
        ).get(CartAndDetailsViewModel::class.java)

        Log.i("teste5", viewModel.listCart.value?.size.toString())

        recyclerViewCart = binding.rvCart
        cartAdapter = CartAdapter()
        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
    }
}