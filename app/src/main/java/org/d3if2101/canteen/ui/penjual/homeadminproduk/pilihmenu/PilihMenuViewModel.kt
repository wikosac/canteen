package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.Produk
import org.d3if2101.canteen.data.repository.CanteenRepository

class PilihMenuViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    private val produkLiveData: MutableLiveData<List<Produk>> = MutableLiveData()
    private val stringReceived: MutableLiveData<String> = MutableLiveData()

    fun deleteProdukByID(idProduk: String): LiveData<Message> {
        getDataFromDB()
        return canteenRepository.deleteProductByID(idProduk)
    }

    fun getDataFromDB() {
        canteenRepository.getProdukFromDB().observeForever {
            val filteredList = it.filter { produk -> stringReceived.value == produk.jenis }
            produkLiveData.postValue(filteredList)
        }
    }

    fun setStringReceived(data: String) {
        stringReceived.value = data
    }



    fun inputProduktoDB(
        namaProduk: String,
        jenis: String,
        harga: String,
        image: String,
        stock: String
    ): LiveData<Message> {
        return canteenRepository.recoverProductDB(
            namaProduk,
            jenis,
            harga,
            image,
            stock
        )
    }


    fun getFilteredData(): LiveData<List<Produk>> = produkLiveData


}