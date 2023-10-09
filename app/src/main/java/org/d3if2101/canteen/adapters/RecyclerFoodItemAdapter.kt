package org.d3if2101.canteen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.Produk
import java.util.Locale

class RecyclerFoodItemAdapter (
    var context: Context,
    private var itemList: ArrayList<Produk>,
    private val loadDefaultImage: Int,
    val listener: onItemClickListener
):
        RecyclerView.Adapter<RecyclerFoodItemAdapter.ItemListViewHolder>(), Filterable {

    private var fullItemList = ArrayList<Produk>(itemList)

    interface onItemClickListener {
        fun onItemClick(item: Produk)
        fun onPlusBtnClick(item: Produk)
        fun onMinusBtnClick(item: Produk)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemNameTv: TextView = itemView.findViewById(R.id.item_name)
        val itemPriceTv: TextView = itemView.findViewById(R.id.item_price)
        val itemStarsTv: TextView = itemView.findViewById(R.id.item_stars)
        val itemShortDesc: TextView = itemView.findViewById(R.id.item_short_desc)
        val itemQuantityTv: TextView = itemView.findViewById(R.id.item_quantity)
        val itemQuantityIncreaseIV: ImageView =
            itemView.findViewById(R.id.increase_item_quantity_iv)
        val itemQuantityDecreaseIV: ImageView =
            itemView.findViewById(R.id.decrease_item_quantity_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_menu, parent, false)
        fullItemList = ArrayList<Produk>(itemList)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val currentItem = itemList[position]

        if (loadDefaultImage == 1) holder.itemImageTv.setImageResource(R.drawable.default_item_image)
        else Picasso.get().load(currentItem.gambar).into(holder.itemImageTv)

        holder.itemNameTv.text = currentItem.nama
        holder.itemPriceTv.text = "$${currentItem.harga}"
//        holder.itemStarsTV.text = currentItem.itemStars.toString()
//        holder.itemShortDesc.text = currentItem.
        holder.itemQuantityTv.text = currentItem.stok.toString()

        holder.itemQuantityIncreaseIV.setOnClickListener {
            val n = currentItem.stok
            holder.itemQuantityTv.text = (n + 1).toString()

            listener.onPlusBtnClick(currentItem)
        }

        holder.itemQuantityDecreaseIV.setOnClickListener {
            val n = currentItem.stok
            if (n == 0) return@setOnClickListener
            holder.itemQuantityTv.text = (n - 1).toString()

            listener.onMinusBtnClick(currentItem)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun filterList(filteredList: ArrayList<Produk>) {
        itemList = filteredList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return searchFilter;
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<Produk>()
            if (constraint!!.isEmpty()) {
                filteredList.addAll(fullItemList)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (item in fullItemList) {
//                    if (item.itemName.toLowerCase(Locale.ROOT).contains(filterPattern)) {
//                        filteredList.add(item)
//                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
            itemList.clear()
            itemList.addAll(results!!.values as ArrayList<Produk>)
            notifyDataSetChanged()
        }
    }

}