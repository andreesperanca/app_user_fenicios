package com.voltaire.fenicios.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.voltaire.fenicios.adapters.CartAdapter
import com.voltaire.fenicios.databinding.FragmentCartBinding


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

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

        recyclerViewCart = binding.rvCart
        cartAdapter = CartAdapter()
        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
    }
}