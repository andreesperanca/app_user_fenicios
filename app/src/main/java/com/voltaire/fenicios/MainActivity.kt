package com.voltaire.fenicios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var db = FirebaseFirestore.getInstance()
    var auth = Firebase.auth
    lateinit var userLogged : DocumentSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        loadUser()
    }

    private fun loadUser() {

        db.collection("users")
            .document(auth.currentUser!!.uid)
            .addSnapshotListener { user , error ->
                userLogged = user!!
            }

        db.collection("users")
            .document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                userLogged = result
                initialInterface()

            }
            .addOnFailureListener { exception ->
                exception.message?.let { toastCreator(it) }
            }

    }

    private fun initialInterface() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun toastCreator(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}