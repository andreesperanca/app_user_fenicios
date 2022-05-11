package com.voltaire.fenicios.ui_login.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voltaire.fenicios.repositories.HomeRepository
import com.voltaire.fenicios.repositories.LoginRepository

class LoginViewModelFactory constructor(
    private val repository: LoginRepository
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}