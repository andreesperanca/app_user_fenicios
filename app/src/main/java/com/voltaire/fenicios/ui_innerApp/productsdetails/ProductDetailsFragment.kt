package com.voltaire.fenicios.ui_innerApp.productsdetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.BottomAddToCartDialogBinding
import com.voltaire.fenicios.databinding.FragmentProductDetailsBinding
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.repositories.CartAndDetailsRepository
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModel
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModelFactory
import java.util.jar.Manifest

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var viewModel: CartAndDetailsViewModel

    private val args: ProductDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(viewModelStore, CartAndDetailsViewModelFactory(
            CartAndDetailsRepository())).get(CartAndDetailsViewModel::class.java)

        val product = args.product

        with(binding) {
            Glide.with(requireContext())
                .load(product.url)
                .into(detailImage)
            detailImage.setImageResource(R.drawable.mock_product)
            detailTitle.text = product.name
            detailDescription.text = product.description

            detailPrice.text = getString(R.string.defaultPrice, product.prices!!["Pequena"])

            detailSmallPrice.text = getString(R.string.smallPrice, product.prices!!["Pequena"])
            detailAveragePrice.text = getString(R.string.averagePrice, product.prices!!["Media"])
            detailBigPrice.text = getString(R.string.bigPrice, product.prices!!["Grande"])

        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            Log.i("teste", it.size.toString())
        })


        binding.btnCart.setOnClickListener {
            addToCart()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun addToCart() {
        val binding = BottomAddToCartDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(binding.root)

        with(binding) {

            imageBtnSum.setOnClickListener {
                val amount = txtAmount.text.toString().toInt()
                if (amount >= 1) {
                    val result = amount + 1
                    result.toString()
                    txtAmount.text = result.toString()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Você não pode fazer essa ação",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            imageBtnSubtraction.setOnClickListener {
                val amount = txtAmount.text.toString().toInt()
                if (amount > 1) {
                    val result = amount - 1
                    result.toString()
                    txtAmount.text = result.toString()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Você não pode fazer essa ação",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            fun checkBoxReturn(radioGroup: RadioGroup): String {
                if (radioGroup.checkedRadioButtonId == -1) {
                    Toast.makeText(
                        requireContext(),
                        "Selecione uma das opções de tamanho, por favor.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                when (radioGroup.checkedRadioButtonId) {
                    radioBig.id -> return "Grande"
                    radioAverage.id -> return "Média"
                    radioSmall.id -> return "Pequena"
                }
                return "null"
            }

            btnConfirm.setOnClickListener {
                val returnSize = checkBoxReturn(binding.radioGroupSize)
                val amount = binding.txtAmount.text.toString().toInt()
                if (checkBoxReturn(binding.radioGroupSize) != "null") {
                    val newProduct = Product(
                        args.product.name, args.product.prices, args.product.category,
                        args.product.description, args.product.url, amount,
                        amount * (args.product.prices!![returnSize])!!.toInt(),
                        returnSize
                    )
                    val db = FirebaseFirestore.getInstance()
                    val user = (context as MainActivity).userLoggedReal
                    user?.cart?.add(newProduct)


                    db.collection("users")
                        .document((activity as MainActivity).auth.currentUser!!.uid)
                        .update( "cart", user?.cart)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(requireContext(), "item adicionado.", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(requireContext(), "não deu certo", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}