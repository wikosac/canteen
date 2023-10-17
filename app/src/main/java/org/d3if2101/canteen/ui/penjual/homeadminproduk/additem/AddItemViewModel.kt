package org.d3if2101.canteen.ui.penjual.homeadminproduk.additem

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.repository.CanteenRepository

class AddItemViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    fun inputProduktoDB(
        namaProduk: String,
        jenis: String,
        harga: Int,
        image: Uri,
        deskripsi: String,
    ): LiveData<Message> {
        return canteenRepository.inputProdukToDatabase(
            namaProduk,
            jenis,
            harga,
            image,
            deskripsi,
        )
    }


    fun editProductbyID(
        idProduk: String,
        namaProduk: String,
        jenis: String,
        harga: Int,
        image: Uri,
        desc: String
    ): LiveData<Message> {
        return canteenRepository.editProductByID(
            idProduk,
            namaProduk,
            jenis,
            harga,
            image,
            desc
        )
    }
}