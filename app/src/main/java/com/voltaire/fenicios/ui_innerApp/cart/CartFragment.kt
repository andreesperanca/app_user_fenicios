package com.voltaire.fenicios.ui_innerApp.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.adapters.CartAdapter
import com.voltaire.fenicios.adapters.CartAdapterCallBacks
import com.voltaire.fenicios.databinding.FragmentCartBinding
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.model.User
import com.voltaire.fenicios.repositories.CartAndDetailsRepository
import com.voltaire.fenicios.ui_innerApp.home.HomeFragmentDirections
import kotlinx.coroutines.tasks.await


class CartFragment() : Fragment() , CartAdapterCallBacks {

    private lateinit var binding: FragmentCartBinding

    private lateinit var viewModel: CartAndDetailsViewModel

    private lateinit var recyclerViewCart: RecyclerView

    private lateinit var cartAdapter: CartAdapter

    private var cartPrice : Float = 0.00f

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()


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
    }

    override fun onStart() {
        super.onStart()
        loadCart()

        binding.btnBuy.setOnClickListener { it: View? ->
            val action = CartFragmentDirections.actionCartFragmentToPurchaseFragment(cartPrice.toFloat())
            it?.findNavController()?.navigate(action)
        }
    }

    private fun loadCart() {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                val user = result.toObject(User::class.java)
                val listCart = user?.cart
                recyclerViewCart = binding.rvCart
                cartAdapter = CartAdapter(listCart!!, listener = this)
                recyclerViewCart.adapter = cartAdapter
                recyclerViewCart.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
                cartPrice = 0.00f

                listCart.forEach {
                    cartPrice += it.finalPrice!!.toFloat()
                }
                binding.txtValor.text = context?.getString(R.string.cartValor, cartPrice.toFloat().toString())
            }
            .addOnFailureListener { exception ->
                TODO()
            }
    }

    override fun ExcludeItemListener(product: Product) {
        excludeItem(product)
    }

    private fun excludeItem(product: Product) {

        val user = (context as MainActivity).userLoggedReal
        user?.cart?.remove(product)
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .update("cart", user?.cart)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Removido item.", Toast.LENGTH_SHORT).show()
                    loadCart()
                } else {
                    Toast.makeText(requireContext(), "não deu certo", Toast.LENGTH_SHORT).show()
                }
            }
    }
}