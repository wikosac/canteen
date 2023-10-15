package org.d3if2101.canteen.ui.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class OrderViewModel(private val canteenRepository: CanteenRepository): ViewModel() {

    fun insertOrderRecord(order: OrderHistoryItem) {
       canteenRepository.insertOrderRecord(order)
    }

    fun getOrderRecord(): LiveData<List<OrderHistoryItem>> = canteenRepository.getOrderRecord()
}