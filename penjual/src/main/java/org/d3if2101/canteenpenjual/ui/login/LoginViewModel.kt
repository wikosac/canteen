package org.d3if2101.canteenpenjual.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if2101.canteenpenjual.data.model.Message
import org.d3if2101.canteenpenjual.data.repository.CanteenRepository
import org.d3if2101.canteenpenjual.ui.SettingPreferences

class LoginViewModel(private val canteenRepository: CanteenRepository, private val pref: SettingPreferences) : ViewModel() {

    fun loginUser(email: String, pass: String): LiveData<Message> {
        return canteenRepository.loginUser(email, pass)
    }

    fun getTokenPref(): LiveData<String?> = pref.getToken().asLiveData()

    fun saveTokenPref(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
        Log.d("testo", "saveTokenPref: token saved!")
    }

    fun deleteTokenPref() {
        viewModelScope.launch {
            pref.deleteToken()
        }
    }

}
