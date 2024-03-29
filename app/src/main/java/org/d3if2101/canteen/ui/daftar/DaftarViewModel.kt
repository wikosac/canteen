package org.d3if2101.canteen.ui.daftar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.repository.CanteenRepository

class DaftarViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    fun signUpUser(
        email: String,
        pass: String,
    ): LiveData<Message> {
        return canteenRepository.registUser(email, pass)
    }

    fun insertToDB(
        email: String,
        nama: String,
        noTelpon: String,
        role: String
    ): LiveData<Message> {
        return canteenRepository.inputUserToDatabase(email, nama, noTelpon, "foto", role)
    }
}