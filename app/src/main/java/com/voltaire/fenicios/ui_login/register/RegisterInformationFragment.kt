package com.voltaire.fenicios.ui_login.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.FragmentRegisterInformationsBinding
import com.voltaire.fenicios.model.User

class RegisterInformationFragment : Fragment() {

    private lateinit var binding: FragmentRegisterInformationsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val args: RegisterInformationFragmentArgs by navArgs()
    private lateinit var uidUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterInformationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        uidUser = args.uid
    }

    override fun onStart() {
        super.onStart()

        binding.confirmRegister.setOnClickListener {
            registerUserInFirebase()
        }
        binding.connectOtherNumber.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_registerInformations_to_loginFragment)
        }


    }

    private fun registerUserInFirebase() {
        if (verifyRegister()) {
            val txtName = binding.txtName.text.toString()
            val txtEmail = binding.txtEmail.text.toString()

            val phoneNumber = auth.currentUser?.phoneNumber
            val newUser = User(uidUser, txtName, phoneNumber.toString(), txtEmail)

            db.collection("users")
                .document(uidUser)
                .set(newUser)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()

                    } else {
                        toastCreator("Falha interna no servidor")
                    }
                }
        }
    }

    private fun verifyRegister(): Boolean {
        val txtName = binding.txtName.text
        val txtEmail = binding.txtEmail.text
        if (txtName != null && txtName.isNotEmpty() && txtName.isNotBlank() &&
            txtEmail != null && txtEmail.isNotEmpty() && txtEmail.isNotBlank()
        ) {
            return true
        } else {
            toastCreator("Ei amigo, digite um nome e email válido por favor.")
            return false
        }
    }

    private fun toastCreator(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }
}