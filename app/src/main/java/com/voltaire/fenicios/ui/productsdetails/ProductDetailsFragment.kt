package com.voltaire.fenicios.ui.productsdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.FragmentProductDetailsBinding
import com.voltaire.fenicios.model.CartItem

class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private lateinit var binding: FragmentProductDetailsBinding
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
        val product = args.product

        with(binding) {
            detailImage.setImageResource(R.drawable.mock_product)
            detailPrice.text = product.price.toString()
            detailTitle.text = product.name
            detailDescription.text = product.category
        }
    }

    override fun onStart() {
        super.onStart()


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

        val txtAmount = bottomDialog.findViewById<TextView>(R.id.txtAmount)

        editBtnSum.setOnClickListener {
            val amount = txtAmount.text.toString().toInt()
            if (amount >= 1 ) {
                val result = amount + 1
                result.toString()
                txtAmount.text = result.toString()
            } else {
                Toast.makeText(requireContext(), "Você não pode fazer essa ação", Toast.LENGTH_SHORT).show()
            }
        }

        editBtnSubtraction.setOnClickListener {
            val amount = txtAmount.text.toString().toInt()
            if (amount > 1 ) {
                val result = amount - 1
                result.toString()
                txtAmount.text = result.toString()
            } else {
                Toast.makeText(requireContext(), "Você não pode fazer essa ação", Toast.LENGTH_SHORT).show()
            }
        }

        editBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        fun isCheckedBox () : String {


            return
        }
        editBtnConfirm.setOnClickListener {
            println(radioSmall.isChecked.toString())
            println(radioAverage.isChecked.toString())
            println(radioBig.isChecked.toString())
        }


        dialog.show()
    }
}