package org.d3if2101.canteen.ui.penjual.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.CartItem

class CardHistoryItemAdapter(
    private var itemList: List<CartItem>,
) : RecyclerView.Adapter<CardHistoryItemAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.txt_nama_produk)
        val qty: TextView = itemView.findViewById(R.id.txt_jumlah_produk)
        val price: TextView = itemView.findViewById(R.id.txt_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk_riwayat, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.productName.text = currentItem.itemName
        holder.qty.text = currentItem.quantity.toString()
        holder.price.text = "Rp ${currentItem.itemPrice}"
    }

    override fun getItemCount(): Int = itemList.size

}