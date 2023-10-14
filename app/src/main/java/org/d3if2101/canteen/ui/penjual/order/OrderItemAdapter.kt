package org.d3if2101.canteen.ui.penjual.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class OrderItemAdapter(
    private var riwayatList: List<OrderHistoryItem>,
    ) : RecyclerView.Adapter<OrderItemAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produk_riwayat, parent, false)
        return RiwayatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = riwayatList.size
}
