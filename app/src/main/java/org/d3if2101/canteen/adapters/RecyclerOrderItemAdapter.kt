package org.d3if2101.canteen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.CartItem

class RecyclerOrderItemAdapter(
    var context: Context,
    private val itemOrderedList: ArrayList<CartItem>,
    private val activityTotalItemTV: TextView,
    totalItem: Int,
    private val activityTotalPriceTV: TextView,
    totalPrice: Int,
    private val activityProceedToPayBtn: Button,
    private val loadDefaultImage: Int,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerOrderItemAdapter.ItemListViewHolder>() {

    private var totalPrice: Int = totalPrice
    private var totalItems = totalItem

    interface OnItemClickListener {
        fun emptyOrder()
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageIV: ImageView = itemView.findViewById(R.id.item_image)
        val removeOrderedItem: LinearLayout = itemView.findViewById(R.id.remove_ordered_item)
        val itemNameTV: TextView = itemView.findViewById(R.id.item_name)
        val itemPriceTV: TextView = itemView.findViewById(R.id.item_price)
        val itemStarsTV: TextView = itemView.findViewById(R.id.item_stars)
        val itemShortDesc: TextView = itemView.findViewById(R.id.item_short_desc)
        val itemQuantityTV: TextView = itemView.findViewById(R.id.item_quantity_tv)
        val itemQuantityIncreaseIV: ImageView = itemView.findViewById(R.id.increase_item_quantity_iv)
        val itemQuantityDecreaseIV: ImageView = itemView.findViewById(R.id.decrease_item_quantity_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_menu_item, parent, false)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val currentItem = itemOrderedList[position]

        holder.removeOrderedItem.visibility = ViewGroup.VISIBLE

        if(loadDefaultImage == 1) holder.itemImageIV.setImageResource(R.drawable.default_item_image)
        else Picasso.get().load(currentItem.imageUrl).into(holder.itemImageIV)

        holder.itemNameTV.text = currentItem.itemName
        holder.itemPriceTV.text = context.getString(R.string.rupiah, currentItem.itemPrice)
        holder.itemStarsTV.text = currentItem.itemStars.toString()
        holder.itemShortDesc.text = currentItem.itemShortDesc

        holder.itemQuantityTV.text = currentItem.quantity.toString()

        holder.removeOrderedItem.setOnClickListener {
            totalItems -= currentItem.quantity
            totalPrice -= currentItem.quantity * currentItem.itemPrice
            removeItem(position)
            updateOrderDetails()
        }

        holder.itemQuantityIncreaseIV.setOnClickListener {
            val n = currentItem.quantity
            holder.itemQuantityTV.text = (n+1).toString()
            currentItem.quantity++

            ++totalItems
            totalPrice += currentItem.itemPrice
            updateOrderDetails()
        }

        holder.itemQuantityDecreaseIV.setOnClickListener {
            val n = currentItem.quantity
            if(n!=0) {
                holder.itemQuantityTV.text = (n-1).toString()

                if(n-1 == 0) {
                    totalItems -= currentItem.quantity
                    totalPrice -= currentItem.itemPrice * currentItem.quantity
                    removeItem(position)
                } else {
                    --totalItems
                    totalPrice -= currentItem.itemPrice
                }

                currentItem.quantity--
                updateOrderDetails()
            }
        }
    }

    private fun removeItem(position: Int) {
        itemOrderedList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        if(itemCount == 0) {
            listener.emptyOrder()
        }
    }

    override fun getItemCount(): Int = itemOrderedList.size

    private fun updateOrderDetails() {
        activityTotalItemTV.text = context.getString(R.string.jumlah_produk, totalItems)
        activityTotalPriceTV.text = context.getString(R.string.rupiah, totalPrice)
        activityProceedToPayBtn.text = context.getString(R.string.bayar, totalPrice)
    }

    fun getTotalItemPrice(): Int = totalPrice
}
