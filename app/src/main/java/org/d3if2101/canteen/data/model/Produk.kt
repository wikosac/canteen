package org.d3if2101.canteen.data.model

// Data class model untuk tabel Makanan
data class Produk(
    var sellerID: String = "SELLER_ID",
    var itemID: String = "ITEM_ID",
    var imageUrl: String = "IMAGE_URL",
    var itemName: String = "ITEM_NAME",
    var itemPrice: Float = 0.0F,
    var itemShortDesc: String = "ITEM_DESC",
    var itemTag: String = "ITEM_TAG",
    var itemStars: Float = 5.0F,
    var quantity: Int = 0
)
