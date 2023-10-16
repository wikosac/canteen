package org.d3if2101.canteen.ui.pesanan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.datamodels.OrderDetail
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class CurrentOrderItemAdapter(
    private var itemList: List<OrderDetail>,
    private val viewModel: OrderViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<CurrentOrderItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.txt_nama_produk)
        val qty: TextView = itemView.findViewById(R.id.txt_terjual)
        val price: TextView = itemView.findViewById(R.id.txt_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat_transaksi, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]

        viewModel.getProductFromID(currentItem.productId).observe(lifecycleOwner) {
            holder.productName.text = it.itemName
            holder.qty.text = currentItem.qtyOrder.toString()
            holder.price.text = (it.itemPrice * currentItem.qtyOrder).toString()
        }
    }

    override fun getItemCount(): Int = itemList.size
}