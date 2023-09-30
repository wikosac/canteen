package adapters

import android.content.Context
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import java.util.*
import kotlin.collections.ArrayList

class RecyclerFoodItemAdapter (
    var context: Context,
            private var itemList: ArrayList<MenuItem>,
            private val loadDefaultImage: Int,
            val listener: onItemClickListener ):

        RecyclerView.Adapter<RecyclerFoodItemAdapter.ItemListViewHolder>(), Filterable {

    private var fullItemList = ArrayList<MenuItem>(itemList)

    interface onItemClickListener {
        fun onItemClick(item: MenuItem)
        fun onPlusBtnClick(item: MenuItem)
        fun onMinusBtnClick(item: MenuItem)
    }

    class ItemListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImageTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemNameTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemPriceTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemStarsTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemShortDesc: ImageView = itemView.findViewById(R.id.item_image)
        val itemQuantityTv: ImageView = itemView.findViewById(R.id.item_image)
        val itemQuantityIncreaseIV: ImageView =
            itemView.findViewById(R.id.increase_item_quantity_iv)
        val itemQuantityDecreaseIV: ImageView =
            itemView.findViewById(R.id.decrease_item_quantity_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_menu, parent, false)
        fullItemList = ArrayList<MenuItem>(itemList)
        return ItemListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val currentItem = itemList[position]

        if (loadDefaultImage == 1) holder.itemImageTv.setImageResource(R.drawable.default_item_image)
        else Picasso.get().load(currentItem.imageUrl).into(holder.itemImageTv)

        holder.itemNameTv.text = currentItem.itemName
        holder.itemPriceTv.text = "$${currentItem.itemPrice}"
        holder.itemStarsTV.text = currentItem.itemStars.toString()
        holder.itemShortDesc.text = currentItem.itemShortDesc
        holder.itemQuantityTV.text = currentItem.quantity.toString()

        holder.itemQuantityIncreaseIV.setOnClickListener {
            val n = currentItem.quantity
            holder.itemQuantityTV.text = (n + 1).toString()

            listener.onPlusBtnClick(currentItem)
        }

        holder.itemQuantityDecreaseIV.setOnClickListener {
            val n = currentItem.quantity
            if (n == 0) return@setOnClickListener
            holder.itemQuantityTV.text = (n - 1).toString()

            listener.onMinusBtnClick(currentItem)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun filterList(filteredList: ArrayList<MenuItem>) {
        itemList = filteredList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return searchFilter;
    }

    private val searchFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = ArrayList<MenuItem>()
            if (constraint!!.isEmpty()) {
                filteredList.addAll(fullItemList)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (item in fullItemList) {
                    if (item.itemName.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

    override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
        itemList.clear()
        itemList.addAll(results!!.values as ArrayList<MenuItem>)
        notifyDataSetChanged()
    }

}

}