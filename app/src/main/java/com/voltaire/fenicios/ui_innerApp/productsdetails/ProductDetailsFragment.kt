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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.FragmentProductDetailsBinding
import com.voltaire.fenicios.model.CartItem
import com.voltaire.fenicios.repositories.CartAndDetailsRepository
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModel
import com.voltaire.fenicios.ui_innerApp.cart.CartAndDetailsViewModelFactory

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var viewModel : CartAndDetailsViewModel

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

        viewModel = ViewModelProvider(
            viewModelStore,
            CartAndDetailsViewModelFactory(
                CartAndDetailsRepository()
            )
        ).get(CartAndDetailsViewModel::class.java)

        val product = args.product

        with(binding) {
            detailImage.setImageResource(R.drawable.mock_product)
            detailTitle.text = product.name
            detailDescription.text = product.category
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
        val bottomDialog = layoutInflater.inflate(R.layout.bottom_add_to_cart_dialog, null)
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
        dialog.setContentView(bottomDialog)

        val editBtnConfirm = bottomDialog.findViewById<Button>(R.id.btnConfirm)
        val editBtnCancel = bottomDialog.findViewById<Button>(R.id.btnCancel)
        val editBtnSum = bottomDialog.findViewById<ImageButton>(R.id.imageBtnSum)
        val editBtnSubtraction = bottomDialog.findViewById<ImageButton>(R.id.imageBtnSubtraction)

        val radioSmall = bottomDialog.findViewById<RadioButton>(R.id.radioSmall)
        val radioBig = bottomDialog.findViewById<RadioButton>(R.id.radioBig)
        val radioAverage = bottomDialog.findViewById<RadioButton>(R.id.radioAverage)
        val radioGroup = bottomDialog.findViewById<RadioGroup>(R.id.radioGroupSize)

        val txtAmount = bottomDialog.findViewById<TextView>(R.id.txtAmount)

        editBtnSum.setOnClickListener {
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

        editBtnSubtraction.setOnClickListener {
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

        fun checkBoxReturn (radioGroup : RadioGroup) : String {
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

        editBtnConfirm.setOnClickListener {
            val amount = txtAmount.text.toString()
            val priceFinal = amount.toInt() * args.product.list[0].price.toInt()


            val list = mutableListOf<CartItem>()
            val newCartItem = CartItem(args.product.name, args.product.list[0].price, amount.toInt(), priceFinal.toDouble())
            list.add(newCartItem)
            Log.i("teste2", list.size.toString())
            viewModel.addItemCartList(newCartItem)
            dialog.dismiss()

        }

        editBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}