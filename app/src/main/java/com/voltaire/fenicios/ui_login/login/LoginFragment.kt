package com.voltaire.fenicios.ui_login.login

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.LoginActivity
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.R
import com.voltaire.fenicios.databinding.FragmentLoginBinding
import com.voltaire.fenicios.model.User
import com.voltaire.fenicios.ui_innerApp.home.HomeFragment
import com.voltaire.fenicios.ui_login.confirmNumber.ConfirmNumberFragmentDirections
import io.grpc.InternalChannelz.id

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        progressBar = binding.progressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding.editNumberLogin.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
        binding.btnEnter.setOnClickListener {
            if (verifyNumber()) {
                val editTxt = binding.editNumberLogin.text
                val numberPhone = "+55 ${editTxt}"
                val action =
                    LoginFragmentDirections.actionLoginFragmentToConfirmNumberFragment(numberPhone)
                findNavController().navigate(action)

            } else {
                toastCreator("Digite um número válido com DDD, por favor.")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        verifyAuth()

    }

fun verifyAuth() {
    if (auth.currentUser != null) {
        startLoading(true)
        val currentUser = auth.currentUser
        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                if (auth.currentUser != null) {
                    val userLogged = it.toObject(User::class.java)?.name
                    if (userLogged != null) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        startLoading(false)
                    } else {
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToRegisterInformations2(
                                currentUser!!.uid
                            )
                        findNavController().navigate(action)
                        startLoading(false)
                    }
                }
            }.addOnFailureListener {
                toastCreator(it.message.toString())
                startLoading(false)
            }
    }
}

private fun verifyNumber(): Boolean {
    val txtNumber = binding.editNumberLogin

    if (txtNumber.length() != 13) {
        toastCreator("Digite um número válido, com DDD por favor.")
        return false
    } else {
        return true
    }
}

private fun toastCreator(s: String) {
    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
}

private fun updateUI(user: FirebaseUser? = auth.currentUser) {
    val intent = Intent(requireContext(), MainActivity::class.java)
    startActivity(intent)
    activity?.finish()
}

private fun startLoading(status: Boolean) {
    if (status) {
        binding.progressBar.visibility = View.VISIBLE
    } else {
        binding.progressBar.visibility = View.INVISIBLE
    }
}
}
