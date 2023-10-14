package org.d3if2101.canteen.datamodels

data class OrderHistoryItem(
    var id: Int = 0,
    var orderId: String = "ORDER_ID",
    var date: String = "DATE",
    var orderStatus: String = "ORDER_STATUS",
    var orderPayment: String = "ORDER_PAYMENT",
    var price: String = "ORDER_PRICE",
    var productIDs: List<OrderDetail> = listOf(OrderDetail("", 0)),
    var buyerUid: String = "BUYER_UID",
    var sellerUid: String = "SELLER_UID",
    var rating: Float = 5.0F,
    var review: String = "REVIEW",
    var productID: String = "PRODUCT_ID",
    var quantity: Int = 0
)

data class OrderDetail(
    var productId: String = "",
    var qtyOrder: Int = 0
)