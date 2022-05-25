package com.voltaire.fenicios.ui_innerApp.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.voltaire.fenicios.R
import com.voltaire.fenicios.adapters.CategoriesAdapterCallBacks
import com.voltaire.fenicios.adapters.RequestsAdapter
import com.voltaire.fenicios.databinding.FragmentRequestBinding
import com.voltaire.fenicios.model.Purchase

class RequestFragment : Fragment() {

    private lateinit var binding : FragmentRequestBinding

    private lateinit var rv : RecyclerView
    private lateinit var adapter : RequestsAdapter

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        db.collection("pedidos")
            .document("pendentes")
            .collection(auth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val requests = it.result.toObjects(Purchase::class.java)

                    rv = binding.rvRequests
                    adapter = RequestsAdapter(requests)
                    rv.adapter = adapter
                    rv.layoutManager = LinearLayoutManager(requireContext(), VERTICAL , false)
                }
            }
    }
}