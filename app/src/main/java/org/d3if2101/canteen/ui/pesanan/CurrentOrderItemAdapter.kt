package org.d3if2101.canteen.ui.pesanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class CurrentOrderItemAdapter(
    private var itemList: List<OrderHistoryItem>
) : RecyclerView.Adapter<CurrentOrderItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.txt_nama_produk)
        val qty: TextView = itemView.findViewById(R.id.txt_jumlah_produk)
        val price: TextView = itemView.findViewById(R.id.txt_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk_riwayat, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.productName.text = currentItem.date
        holder.qty.text = currentItem.quantity.toString()
        holder.price.text = currentItem.price
    }

    override fun getItemCount(): Int = itemList.size
}