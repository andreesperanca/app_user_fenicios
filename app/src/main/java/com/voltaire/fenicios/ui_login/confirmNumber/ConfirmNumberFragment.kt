package com.voltaire.fenicios.ui_login.confirmNumber

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.LoginActivity
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.databinding.FragmentConfirmNumberBinding
import com.voltaire.fenicios.model.User
import java.util.concurrent.TimeUnit


class ConfirmNumberFragment : Fragment() {

    private lateinit var binding : FragmentConfirmNumberBinding

    private lateinit var auth : FirebaseAuth
    lateinit var credential : PhoneAuthCredential

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val args: ConfirmNumberFragmentArgs by navArgs()
    private var numberPhone : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmNumberBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberPhone = args.numberPhone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = FragmentConfirmNumberBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    toastCreator("Requisição inválida")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    toastCreator("A cota de 50 SMS por dia foi excedida")
                }
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                Log.d(TAG, "onCodeSent:$verificationId")

                storedVerificationId = verificationId
                resendToken = token

                binding.btnConfirm.setOnClickListener {
                    val code = binding.txtConfirmNumber.text.toString()
                    println(code)
                    verifyPhoneNumberWithCode(storedVerificationId, code)
                    signInWithPhoneAuthCredential(credential)
                }
                binding.btnResend.setOnClickListener {
                    resendVerificationCode(numberPhone,resendToken)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startPhoneNumberVerification(numberPhone)
    }


    private fun toastCreator(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
    }
    private fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as LoginActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as LoginActivity) { login ->
                if (login.isSuccessful) {
                    val user = login.result?.user
                    register(user)
                } else {
                    toastCreator("Falha no login, motivo: ${login.exception?.message}")
                    if (login.exception is FirebaseAuthInvalidCredentialsException) {
                        toastCreator("Código de verificação está incorreto")
                    }
                    // Update UI
                }
            }
    }
    private fun updateUI(user: User?) {
        val intent = Intent(context as LoginActivity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context as LoginActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun register(user : FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { it: DocumentSnapshot? ->
                val userTest = it?.toObject(User::class.java)?.name
                if (userTest != null) {
                    updateUI(it?.toObject(User::class.java))
                } else {
                    val action = ConfirmNumberFragmentDirections.actionConfirmNumberFragmentToRegisterInformations(user!!.uid)
                    findNavController().navigate(action)
                }
            }.addOnFailureListener {
                toastCreator(it.message.toString())
            }
    }
}