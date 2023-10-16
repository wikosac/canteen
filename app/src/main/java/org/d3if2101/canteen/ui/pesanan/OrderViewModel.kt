package org.d3if2101.canteen.ui.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class OrderViewModel(private val canteenRepository: CanteenRepository): ViewModel() {

    private val _orders = MutableLiveData<List<OrderHistoryItem>>()
    val orders: LiveData<List<OrderHistoryItem>> = _orders

    fun insertOrderRecord(order: OrderHistoryItem) {
       canteenRepository.insertOrderRecord(order)
    }

    fun getOrderRecord(): LiveData<List<OrderHistoryItem>> = canteenRepository.getOrderRecord()

//    fun getOrders() {
//        _orders.value = canteenRepository.getOrderRecord()
//    }

    fun getProdukWithID(id: String): LiveData<MenuItem> = canteenRepository.getProdukWithID(id)

    fun getFirebaseAuthUID() : LiveData<String> = canteenRepository.getUIDUser()
}