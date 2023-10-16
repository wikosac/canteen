package org.d3if2101.canteen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.penjual.order.CardHistoryItemAdapter
import org.d3if2101.canteen.ui.penjual.order.OrderItemAdapter
import org.d3if2101.canteen.ui.pesanan.CurrentOrderItemAdapter
import org.d3if2101.canteen.ui.pesanan.OrderViewModel

class RecyclerCurrentOrderAdapter(
    var context: Context,
    private var currentOrderList: List<OrderHistoryItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerCurrentOrderAdapter.ItemListViewHolder>() {

    interface OnItemClickListener {
        fun showQRCode(orderID: String)
        fun cancelOrder(position: Int)
    }

    fun updateData(newData: List<OrderHistoryItem>) {
        val diffResult = DiffUtil.calculateDiff(CurrentOrderDiffCallback(currentOrderList, newData))
        currentOrderList = newData
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val takeAwayTimeTV: TextView = itemView.findViewById(R.id.current_order_item_take_away_time_tv)
        val paymentStatusTV: TextView = itemView.findViewById(R.id.current_order_item_payment_status_tv)
        val orderIDTV: TextView = itemView.findViewById(R.id.current_order_item_order_id_tv)
        val totalItemPriceTV: TextView = itemView.findViewById(R.id.current_order_item_total_price_tv)
        val showQRBtn: ExtendedFloatingActionButton = itemView.findViewById(R.id.current_order_item_show_qr_btn)
        val cancelBtn: ExtendedFloatingActionButton = itemView.findViewById(R.id.current_order_item_cancel_btn)
        val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_current_order_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.current_order_item,
            parent,
            false
        )
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {

        val currentItem = currentOrderList[position]

        with (holder) {
            takeAwayTimeTV.text = currentItem.date
            paymentStatusTV.text = currentItem.orderStatus
            orderIDTV.text = currentItem.orderId
            totalItemPriceTV.text = currentItem.price
            showQRBtn.setOnClickListener {
                listener.showQRCode(currentItem.orderId)
            }
            cancelBtn.setOnClickListener {
                listener.cancelOrder(position)
            }
            itemRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = CurrentOrderItemAdapter(currentOrderList)
            }
        }
    }

    override fun getItemCount(): Int = currentOrderList.size

    class CurrentOrderDiffCallback(
        private val oldList: List<OrderHistoryItem>,
        private val newList: List<OrderHistoryItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Implement your logic to check if items are the same.
            // Typically, you would compare item IDs or unique identifiers.
            return oldList[oldItemPosition].orderId == newList[newItemPosition].orderId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // Implement your logic to check if item contents are the same.
            // This is used to detect changes within the same item (e.g., properties of the item).
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    companion object {
        const val TAG = "testo"
    }
}