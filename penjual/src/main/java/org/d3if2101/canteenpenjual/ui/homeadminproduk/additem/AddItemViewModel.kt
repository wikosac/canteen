package org.d3if2101.canteenpenjual.ui.homeadminproduk.additem

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteenpenjual.data.model.Message
import org.d3if2101.canteenpenjual.data.repository.CanteenRepository

class AddItemViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    fun inputProduktoDB(
        namaProduk: String,
        jenis: String,
        harga: String,
        image: Uri,
        stock: String
    ): LiveData<Message> {
        return canteenRepository.inputProdukToDatabase(
            namaProduk,
            jenis,
            harga,
            image,
            stock
        )
    }
}