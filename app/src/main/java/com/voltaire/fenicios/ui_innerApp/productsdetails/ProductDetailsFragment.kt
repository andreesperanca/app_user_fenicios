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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.CartAddItemDialogBinding
import com.voltaire.fenicios.databinding.FragmentProductDetailsBinding
import com.voltaire.fenicios.model.Product
import com.voltaire.fenicios.repositories.CartAndDetailsRepository
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModel
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModelFactory

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
            detailAveragePrice.text = getString(R.string.averagePrice, product.prices!!["M??dia"])
            detailBigPrice.text = getString(R.string.bigPrice, product.prices!!["Grande"])

        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
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

        val binding = CartAddItemDialogBinding.inflate(layoutInflater)
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
                        "Voc?? n??o pode fazer essa a????o.",
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
                        "Voc?? n??o pode fazer essa a????o",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnConfirm.setOnClickListener {
                val returnSize = checkBoxReturn(binding.radioGroupSize, binding.radioBig, binding.radioAverage, binding.radioSmall)
                val amount = binding.txtAmount.text.toString().toInt()
                if (returnSize != "null") {
                    val newProduct = Product(
                        args.product.name, args.product.prices, args.product.category,
                        args.product.description, args.product.url, amount,
                        amount * (args.product.prices!![returnSize])!!.toFloat(),
                        returnSize
                    )
                    val db = FirebaseFirestore.getInstance()
                    val user = (context as MainActivity).userLoggedReal
                    user?.cart?.add(newProduct)

                    db.collection("users")
                        .document((activity as MainActivity).auth.currentUser!!.uid)
                        .update("cart", user?.cart)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Produto adicionado ao carrinho.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Erro inesperado.",
                                    Toast.LENGTH_SHORT
                                ).show()
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
    private fun checkBoxReturn (radioGroup: RadioGroup, rBig : RadioButton, rAverage : RadioButton, rSmall : RadioButton): String {
        if (radioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(
                requireContext(),
                "Selecione uma das op????es de tamanho, por favor.",
                Toast.LENGTH_SHORT
            ).show()
        }
        when (radioGroup.checkedRadioButtonId) {
            rBig.id -> return "Grande"
            rAverage.id -> return "M??dia"
            rSmall.id -> return "Pequena"
        }
        return "null"
    }
}