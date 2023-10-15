package org.d3if2101.canteen.datamodels

data class CartItem(
    var itemID: String = "ITEM_ID",
    var imageUrl: String = "IMAGE_URL",
    var itemName: String = "ITEM_NAME",
    var itemPrice: Int = 0,
    var itemShortDesc: String = "ITEM_DESC",
    var itemStars: Float = 5.0F,
    var quantity: Int = 0
)
