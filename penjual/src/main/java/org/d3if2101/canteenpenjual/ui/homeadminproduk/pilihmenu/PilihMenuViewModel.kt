package org.d3if2101.canteenpenjual.ui.homeadminproduk.pilihmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteenpenjual.data.model.Produk
import org.d3if2101.canteenpenjual.data.repository.CanteenRepository

class PilihMenuViewModel(private val canteenRepository: CanteenRepository) : ViewModel() {

    val produkList = mutableListOf(
        Produk("4291891248", "Pisang Goreng", 100000F, 10, "429249", "aaa", "makanan"),
        Produk("4291891248x", "Pisang Gxoreng", 10000F, 10, "AAAA", "429x249", "makanan")
    )

    fun getDataFromDB(): LiveData<List<Produk>> = canteenRepository.getProdukFromDB()

//    fun addProduk(produk: Produk) {
//        produkList.add(produk)
//    }

}