package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.Produk

class PilihMenuAdapter(
    private val data: List<Produk>,
    private val onItemClickCallback: OnItemClickCallback
) :
    RecyclerView.Adapter<PilihMenuAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pilihmenu, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.iv_Produk)
        private val tvName: TextView = itemView.findViewById(R.id.tv_ProdukRv)
        private val tvHarga: TextView = itemView.findViewById(R.id.tv_SubProduk)

        fun bind(dataList: Produk) {

            Picasso.get()
                .load(dataList.imageUrl)
                .resize(100, 100)
                .into(image)

            tvName.text = dataList.itemName
            tvHarga.text = "Rp ${dataList.itemPrice}"

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(dataList)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Produk)
    }
}
