package org.d3if2101.canteen.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.LinkedList
import java.util.Locale

val staticDataSetKategori: List<String> =
    LinkedList(mutableListOf("Pilih Kategori", "Makanan", "Minuman", "Camilan"))

fun convertStringToDate(dateString: String): Date? {
    val format = SimpleDateFormat("MMM dd, yyyy 'pukul' hh:mm a", Locale.US)
    return try {
        format.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}