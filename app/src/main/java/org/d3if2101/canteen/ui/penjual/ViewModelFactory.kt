package org.d3if2101.canteen.ui.penjual

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if2101.canteenpenjual.data.di.Injection.provideRepository
import org.d3if2101.canteenpenjual.data.repository.CanteenRepository
import org.d3if2101.canteen.ui.penjual.daftar.DaftarViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu.PilihMenuViewModel
import org.d3if2101.canteen.ui.penjual.login.LoginViewModel

class ViewModelFactory private constructor(private val canteenRepository: CanteenRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DaftarViewModel::class.java)) {
            return DaftarViewModel(canteenRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(canteenRepository) as T
        } else if (modelClass.isAssignableFrom(PilihMenuViewModel::class.java)) {
            return PilihMenuViewModel(canteenRepository) as T
        } else if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
            return AddItemViewModel(canteenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(provideRepository(context))
        }.also { instance = it }
    }
}