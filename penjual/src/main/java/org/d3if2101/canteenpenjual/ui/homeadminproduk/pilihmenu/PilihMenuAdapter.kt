package org.d3if2101.canteenpenjual.ui.homeadminproduk.pilihmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.data.model.Produk

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
        //        private val image: ImageView = itemView.findViewById(R.id.haImageView)
        private val tvName: TextView = itemView.findViewById(R.id.tv_ProdukRv)

        fun bind(dataList: Produk) {
//            Glide.with(itemView.context)
//                .load(dataList.image)
//                .apply(RequestOptions().override(100, 100))
//                .into(image)
            tvName.text = dataList.nama

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(dataList)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: Produk)
    }
}
