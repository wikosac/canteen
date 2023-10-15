package org.d3if2101.canteen.datamodels

// untukFirebase
data class OrderHistoryItem(
    var id: Int = 0,
    var orderId: String = "ORDER_ID",
    var date: String = "DATE",
    var orderStatus: String = "ORDER_STATUS",
    var orderPayment: String = "ORDER_PAYMENT",
    var price: String = "ORDER_PRICE",
    var buyerUid: String = "BUYER_UID",
    var sellerUid: String = "SELLER_UID",
    var rating: Float = 5.0F,
    var review: String = "REVIEW",
    var quantity: Int = 0,
    var sendNotification: Boolean = false,
    var productIDs: List<OrderDetail> = listOf(OrderDetail())
)

data class OrderDetail(
    var productId: String = "",
    var qtyOrder: Int = 0
)