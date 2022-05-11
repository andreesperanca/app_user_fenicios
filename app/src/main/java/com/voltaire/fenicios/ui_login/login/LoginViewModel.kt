package com.voltaire.fenicios.ui_login.login

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.voltaire.fenicios.repositories.LoginRepository

class LoginViewModel constructor(private val repository: LoginRepository) : ViewModel() {

    fun verifyNumber(txtNumberLogin : EditText, context: Context): Boolean {
        if (txtNumberLogin.length() != 13) {
            toastCreator("Digite um número válido, com DDD por favor.", context)
            return false
        } else {
            return true
        }
    }

    fun toastCreator(s: String, context : Context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

}
