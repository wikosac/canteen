package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.datamodels.MenuItem

class PilihMenuViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    private val produkLiveData: MutableLiveData<List<MenuItem>> = MutableLiveData()
    private val stringReceived: MutableLiveData<String> = MutableLiveData()

    fun deleteProdukByID(idProduk: String): LiveData<Message> {
        getDataFromDB()
        return canteenRepository.deleteProductByID(idProduk)
    }

    fun getDataFromDB() {
        canteenRepository.getProdukFromDB().observeForever {
            val filteredList = it.filter { produk -> stringReceived.value == produk.itemTag }
            produkLiveData.postValue(filteredList)
        }
    }

    fun setStringReceived(data: String) {
        stringReceived.value = data
    }

    fun editStateByID(
        idProduk: String,
        namaProduk: String,
        jenis: String,
        harga: String,
        image: String,
        desc: String,
        state: Boolean
    ): LiveData<Message> {
        return canteenRepository.editState(
            idProduk,
            namaProduk,
            jenis,
            harga,
            image,
            desc,
            state
        )
    }


    fun inputProduktoDB(
        namaProduk: String,
        jenis: String,
        harga: String,
        image: String,
        desc: String,
        state: Boolean
    ): LiveData<Message> {
        return canteenRepository.recoverProductDB(
            namaProduk,
            jenis,
            harga,
            image,
            desc,
            state
        )
    }


    fun getFilteredData(): LiveData<List<MenuItem>> = produkLiveData



}