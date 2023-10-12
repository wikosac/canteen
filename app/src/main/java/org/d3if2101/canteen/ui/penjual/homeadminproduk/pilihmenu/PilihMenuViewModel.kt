package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.Produk
import org.d3if2101.canteen.data.repository.CanteenRepository

class PilihMenuViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    fun deleteProdukByID(idProduk: String): LiveData<Message> =
        canteenRepository.deleteProductByID(idProduk)

    fun getDataFromDB(): LiveData<List<Produk>> = canteenRepository.getProdukFromDB()


}