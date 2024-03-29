package org.d3if2101.canteen.datamodels

data class CurrentOrderItem(
    var orderID: String = "ORDER_ID",
    var takeAwayTime: String = "TAKE_AWAY_TIME",
    var paymentStatus: String = "PAYMENT_STATUS",
    var orderItemNames: String = "ORDER_ITEM_NAMES",
    var orderItemQuantities: String = "ORDER_ITEM_QUANTITIES",
    var totalQuantities: Int = 0,
    var totalItemPrice: Int = 0,
    var tax: String = "TAX",
    var subTotal: String = "SUB_TOTAL",
    var id: Int = 0,
    var idPenjual: String = "id_penjual",
    var idProduct: String = "id_product"
)