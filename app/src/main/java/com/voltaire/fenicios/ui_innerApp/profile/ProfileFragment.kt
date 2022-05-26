package com.voltaire.fenicios.ui_innerApp.profile

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.voltaire.fenicios.LoginActivity
import com.voltaire.fenicios.MainActivity
import com.voltaire.fenicios.databinding.*
import com.voltaire.fenicios.model.Address
import com.voltaire.fenicios.model.User


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attInfosListener()
        loadUserInfo((context as MainActivity).userLoggedReal!!)
    }

    override fun onStart() {
        super.onStart()

        binding.btnLogout.setOnClickListener {
            (context as MainActivity).auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.editNameButton.setOnClickListener {
            editUserName()
        }

        binding.editAddressButton.setOnClickListener {
            editUserAddress()
        }
    }

    private fun editUserAddress() {
        val _binding = ProfileBottomEditAddressBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(_binding.root)

        with(_binding) {
            saveAddress.setOnClickListener {
                saveNewAddress(_binding, dialog)
            }
            cancelBtnEditAddress.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun saveNewAddress(_binding: ProfileBottomEditAddressBinding, dialog: BottomSheetDialog) {
        val street = _binding.editStreet.text.toString()
        val number = _binding.editNumberHouse.text.toString()
        val district = _binding.editDistrict.text.toString()
        val newAddress = Address(street, number, district)

        (activity as MainActivity).db.collection("users")
            .document((activity as MainActivity).auth.currentUser!!.uid)
            .update("address", newAddress)
            .addOnCompleteListener { update ->
                if (update.isSuccessful) {
                    attInfosListener()
                    dialog.dismiss()
                } else {
                    Toast.makeText(activity, update?.exception?.message, Toast.LENGTH_SHORT).show()
                    _binding.saveAddress.isEnabled = true
                    _binding.cancelBtnEditAddress?.isEnabled = true
                    _binding.editDistrict.isEnabled = true
                    _binding.editNumberHouse.isEnabled = true
                    _binding.editStreet.isEnabled = true
                }
            }
    }
    private fun editUserName() {
        val _binding = ProfileBottomEditNameBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(_binding.root)

        with(_binding) {
            editTextName2.setText((activity as MainActivity).userLoggedReal?.name.toString())
            editTextName2.selectAll()

            saveBtnEditName.setOnClickListener {
                saveNameUser(_binding, dialog)
            }
        }
        dialog.show()
    }
    private fun saveNameUser(_binding: ProfileBottomEditNameBinding, dialog: BottomSheetDialog) {

        (activity as MainActivity).db.collection("users")
            .document((activity as MainActivity).auth.currentUser!!.uid)
            .update("name",_binding.editTextName2.text.toString())
            .addOnCompleteListener { update ->
                if (update.isSuccessful) {
                    attInfosListener()
                    dialog.dismiss()
                } else {
                    Toast.makeText(activity, update?.exception?.message, Toast.LENGTH_SHORT).show()
                    _binding.saveBtnEditName.isEnabled = true
                    _binding.cancelBtnEditName?.isEnabled = true
                    _binding.editTextName2.isEnabled = true
                }
            }
    }
    private fun loadUserInfo(user : User) {

        if (user?.address == null || user?.address.toString() == "") {
            binding.address.text = "Não cadastrado."
        } else {
            binding.address.text = "${user?.address.district},${user?.address.street},${user?.address.number}"
        }
        binding.userName.text = user?.name.toString()
        binding.userNumberPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher("BR"))
        binding.userNumberPhone.text = user?.numberPhone.toString()
        binding.userEmail.text = user?.email.toString()
    }
    private fun attInfosListener() {
        val mRefe = (context as MainActivity)
        mRefe.db.collection("users")
            .document(mRefe.auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { result ->
                //TODO
            }
            .addOnFailureListener { exception ->
                //TODO
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val newUser = it.result.toObject(User::class.java)
                    loadUserInfo(newUser!!)
                }
            }
    }
}