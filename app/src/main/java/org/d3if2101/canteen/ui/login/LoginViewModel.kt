package org.d3if2101.canteen.ui.login

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.ui.SettingPreferences

class LoginViewModel(
    private val canteenRepository: CanteenRepository,
    private val pref: SettingPreferences
) : ViewModel() {

    fun loginUser(email: String, pass: String): LiveData<Message> {
        return canteenRepository.loginUser(email, pass)
    }

    fun getUser(lifecycleOwner: LifecycleOwner): LiveData<UserModel> = canteenRepository.getUser(lifecycleOwner)

    fun getUserWithToken(token: String): LiveData<UserModel> = canteenRepository.getUserWithToken(token)

    fun getTokenPref(): LiveData<String?> = pref.getToken().asLiveData()

    fun saveTokenPref(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
        Log.d("testo", "saveTokenPref: $token")
    }

    fun deleteTokenPref() {
        viewModelScope.launch {
            pref.deleteToken()
        }
        val token = pref.getToken().asLiveData().value.toString()
        Log.d("testo", "deleteTokenPref: $token")
    }

}