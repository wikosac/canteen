package org.d3if2101.canteen.ui.pesanan

import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class OrderViewModel(private val canteenRepository: CanteenRepository): ViewModel() {

    fun insertOrderRecord(order: OrderHistoryItem) {
       return canteenRepository.insertOrderRecord(order)
    }
}