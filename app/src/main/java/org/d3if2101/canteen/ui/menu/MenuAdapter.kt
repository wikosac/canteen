package org.d3if2101.canteen.ui.menu

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ListMenuBinding
import org.d3if2101.canteen.model.Produk

class MenuAdapter(
    private val context: Context,
    private val listImages: ArrayList<Produk>
    ) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    class ViewHolder(binding: ListMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        val harga: TextView = itemView.findViewById(R.id.item_price)
        val nama: TextView = itemView.findViewById(R.id.item_name)
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListMenuBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.harga.text = listImages[position].harga.toString()
        holder.nama.text = listImages[position].nama
        Glide.with(context)
            .load(listImages[position].gambar)
            .into(holder.imageView)

        Log.d("testo", "onBindViewHolder: test")
    }

    override fun getItemCount(): Int = listImages.size
}