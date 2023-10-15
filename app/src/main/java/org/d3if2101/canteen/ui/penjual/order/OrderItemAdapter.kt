package org.d3if2101.canteen.ui.penjual.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.pesanan.OrderViewModel

class OrderItemAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: OrderViewModel,
    private var riwayatList: List<OrderHistoryItem>
) : RecyclerView.Adapter<OrderItemAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser: ImageView = itemView.findViewById(R.id.img_user)
        val time: TextView = itemView.findViewById(R.id.txt_waktu)
        val orderId: TextView= itemView.findViewById(R.id.txt_id_order)
        val totalPrice: TextView = itemView.findViewById(R.id.txt_total_price)
        val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_product_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_riwayat, parent, false)
        return RiwayatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val currentOrderItem = riwayatList[position]
        holder.time.text = currentOrderItem.date
        holder.orderId.text = currentOrderItem.orderId
        holder.totalPrice.text = currentOrderItem.price

        val cartItem = mutableListOf<CartItem>()
        for (item in currentOrderItem.productIDs) {
            viewModel.getProdukWithID(item.productId).observe(lifecycleOwner) {
                Log.d(TAG, "onBindViewHolder: $it")
                val menu = CartItem(
                    itemName = it.itemName,
                    itemPrice = it.itemPrice,
                    quantity = it.quantity
                )
                cartItem.add(menu)
            }
        }

        holder.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = CardHistoryItemAdapter(cartItem)
        }

    }

    override fun getItemCount(): Int = riwayatList.size

    companion object {
        const val TAG = "testo"
    }
}
