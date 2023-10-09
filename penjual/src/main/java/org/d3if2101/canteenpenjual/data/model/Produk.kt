package org.d3if2101.canteenpenjual.data.model

// Data class model untuk tabel Makanan
data class Produk(
    val id: String = "",
    val nama: String = "",
    val harga: Float = 0.0f,
    val stok: Int = 0,
    val penjualId: String = "",
    val jenis: String = "",
    val gambar: String = ""
)
