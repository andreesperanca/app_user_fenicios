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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.databinding.FragmentLoginBinding
import com.voltaire.fenicios.model.User
import com.voltaire.fenicios.repositories.HomeRepository
import com.voltaire.fenicios.repositories.LoginRepository
import com.voltaire.fenicios.ui_innerApp.home.HomeViewModel
import com.voltaire.fenicios.ui_innerApp.home.HomeViewModelFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(
            viewModelStore, LoginViewModelFactory(
                LoginRepository(FirebaseService)
            )
        ).get(LoginViewModel::class.java)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        progressBar = binding.progressBar
        binding.editNumberLogin.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
    }

    override fun onStart() {
        super.onStart()
        binding.btnEnter.setOnClickListener {
            if (loginViewModel.verifyNumber(binding.editNumberLogin, requireContext())) {
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

    override fun onResume() {
        super.onResume()
        verifyAuth()
    }

    private fun verifyAuth() {
        if (auth.currentUser != null) {
            startLoading(true)
            val currentUser = auth.currentUser
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
