package org.d3if2101.canteen.ui.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.datamodels.OrderDetail
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class OrderViewModel(private val canteenRepository: CanteenRepository): ViewModel() {

    fun insertOrderRecord(order: OrderHistoryItem) {
       canteenRepository.insertOrderRecord(order)
    }

    fun getOrderRecord(): LiveData<List<OrderHistoryItem>> = canteenRepository.getOrderRecord()

    fun updateOrderStateByID(
        orderId: String,
        orderState: String,
        orderPayment: String,
    ): LiveData<Message> =
        canteenRepository.updateOrderStateByID(orderId, orderState, orderPayment)

    fun getProductFromID(id: String): LiveData<MenuItem> = canteenRepository.getProdukWithID(id)

    fun getFirebaseAuthUID(): LiveData<String> = canteenRepository.getUIDUser()

    fun getUserFromUID(UID: String): LiveData<UserModel> = canteenRepository.getUserWithToken(UID)

    fun deleteOrderByID(idOrder: String): LiveData<Message> {
        return canteenRepository.deleteOrderById(idOrder)
    }
}