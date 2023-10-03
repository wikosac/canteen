package org.d3if2101.canteenpenjual.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteenpenjual.data.model.Message
import org.d3if2101.canteenpenjual.data.repository.CanteenRepository

class LoginViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    fun loginUser(email: String, pass: String): LiveData<Message> {
        return canteenRepository.loginUser(email, pass)
    }

}
