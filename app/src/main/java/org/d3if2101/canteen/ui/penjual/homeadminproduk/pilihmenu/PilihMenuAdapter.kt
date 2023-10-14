package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.MenuItem

class PilihMenuAdapter(
    private val data: List<MenuItem>,
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
        private val setState: SwitchCompat = itemView.findViewById(R.id.button_on)

        fun bind(dataList: MenuItem) {

            Picasso.get()
                .load(dataList.imageUrl)
                .resize(100, 100)
                .into(image)

            tvName.text = dataList.itemName
            tvHarga.text = dataList.itemPrice.toString()
//            if (dataList.status){
//                setState.isChecked = true
//            } else {
//                setState.isChecked = false
//            }

            setState.setOnCheckedChangeListener { _, isChecked ->
                onItemClickCallback.onItemState(isChecked, dataList)
                Log.d("PILIH MENU ADAPTER", isChecked.toString())
            }


            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(dataList)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: MenuItem)
        fun onItemState(data: Boolean, dataProduct: MenuItem)
    }
}
