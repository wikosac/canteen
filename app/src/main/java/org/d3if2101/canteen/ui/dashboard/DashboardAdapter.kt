package org.d3if2101.canteen.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.UserModel

class DashboardAdapter(
    private val data: List<UserModel>,
    private val onItemClickCallback: OnItemClickCallback
) : RecyclerView.Adapter<DashboardAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dashboard_pembeli, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.itemImageView)
        private val tvName: TextView = itemView.findViewById(R.id.itemTextView)

        fun bind(dataList: UserModel) {
            val photo = dataList.foto
            if (photo.lowercase() == "foto") {
                Picasso.get()
                    .load(R.drawable.background_canteen)
                    .resize(100, 100)
                    .into(image)
            } else {
                Picasso.get()
                    .load(dataList.foto)
                    .resize(100, 100)
                    .into(image)
            }


            tvName.text = "Kantin ${dataList.nama}"

            itemView.setOnClickListener {
                onItemClickCallback.onItemClick(dataList)
            }

        }
    }

    interface OnItemClickCallback {
        fun onItemClick(data: UserModel)
    }
}