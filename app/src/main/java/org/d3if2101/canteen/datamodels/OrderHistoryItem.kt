package org.d3if2101.canteen.datamodels

data class OrderHistoryItem(
    var date: String = "DATE",
    var orderId: String = "ORDER_ID",
    var orderStatus: String = "ORDER_STATUS",
    var orderPayment: String = "ORDER_PAYMENT",
    var price: String = "ORDER_PRICE",
    var id: Int = 0,
    var buyerUid: String = "BUYER_UID",
    var sellerUid: String = "SELLER_UID",
    var rating: Float = 5.0F,
    var review: String = "REVIEW"
)
