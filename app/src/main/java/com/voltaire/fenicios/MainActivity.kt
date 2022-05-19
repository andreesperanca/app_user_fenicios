package com.voltaire.fenicios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.voltaire.fenicios.database.FirebaseService
import com.voltaire.fenicios.databinding.ActivityMainBinding
import com.voltaire.fenicios.model.User
import com.voltaire.fenicios.repositories.HomeRepository
import com.voltaire.fenicios.ui_innerApp.home.HomeViewModel
import com.voltaire.fenicios.ui_innerApp.home.HomeViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

    var userLogged : DocumentSnapshot? = null
    var userLoggedReal : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()
    }

    private fun loadUser() {
        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .addSnapshotListener { user , error ->
                userLogged = user!!
            }

        db.collection("users")
            .document(auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                userLogged = result
                userLoggedReal = result.toObject(User::class.java)
                initialInterface()

            }
            .addOnFailureListener { exception ->
                exception.message?.let { toastCreator(it) }
            }
    }

    private fun initialInterface() {
        binding.progressBar.visibility = View.INVISIBLE

        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun toastCreator(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}