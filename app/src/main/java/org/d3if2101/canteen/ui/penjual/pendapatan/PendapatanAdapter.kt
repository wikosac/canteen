package org.d3if2101.canteen.ui.penjual.pendapatan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import java.text.NumberFormat
import java.util.Locale

class PendapatanAdapter(private val data: List<OrderHistoryItem>) :
    RecyclerView.Adapter<PendapatanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idOrder: TextView = itemView.findViewById(R.id.idOrder)
        val dateOrder: TextView = itemView.findViewById(R.id.dateOrder)
        val txtTotal: TextView = itemView.findViewById(R.id.txt_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat_transaksi, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]
        val price = currentItem.price?.replace("Rp ", "")?.replace(",", "")?.toIntOrNull() ?: 0
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        holder.idOrder.text = currentItem.orderId
        holder.dateOrder.text = currentItem.date
        holder.txtTotal.text = numberFormat.format(price)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
