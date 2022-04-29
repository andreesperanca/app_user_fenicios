package com.voltaire.fenicios.ui_login.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.FragmentRegisterWithPhoneBinding
import com.voltaire.fenicios.ui_innerApp.productsdetails.ProductDetailsFragmentArgs

class RegisterWithPhoneFragment : Fragment() {

    private lateinit var binding : FragmentRegisterWithPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterWithPhoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}