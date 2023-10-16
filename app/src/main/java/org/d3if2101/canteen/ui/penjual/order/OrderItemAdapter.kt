package org.d3if2101.canteen.ui.penjual.order

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.pesanan.OrderViewModel

class OrderItemAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: OrderViewModel,
    private var riwayatList: List<OrderHistoryItem>
) : RecyclerView.Adapter<OrderItemAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser: ImageView = itemView.findViewById(R.id.img_user)
        val txtNamaUser: TextView = itemView.findViewById(R.id.txt_nama_user)
        val time: TextView = itemView.findViewById(R.id.txt_waktu)
        val orderId: TextView = itemView.findViewById(R.id.txt_id_order)
        val totalPrice: TextView = itemView.findViewById(R.id.txt_total_price)
        val itemRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_product_item)
        val btnProcess: Button = itemView.findViewById(R.id.processOrderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_riwayat, parent, false)
        return RiwayatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val currentOrderItem = riwayatList[position]
        val buyerUID = currentOrderItem.buyerUid
        holder.time.text = currentOrderItem.date
        holder.orderId.text = currentOrderItem.orderId
        holder.totalPrice.text = currentOrderItem.price

        viewModel.getUserFromUID(buyerUID).observe(lifecycleOwner) {
            holder.txtNamaUser.text = "Pembeli: ${it.nama}"
        }

        // Use Glide to load an image
        Glide.with(holder.itemView)
            .load(R.drawable.ic_baseline_fastfood_24)
            .override(100, 100)
            .into(holder.imgUser)
        val cartItem = mutableListOf<CartItem>()

        // Use a LiveData builder to observe the data
        currentOrderItem.productIDs.forEach { product ->
            viewModel.getProductFromID(product.productId).observe(lifecycleOwner) { menuItem ->
                CoroutineScope(Dispatchers.IO).launch {
                    val menu = CartItem(
                        imageUrl = menuItem.imageUrl,
                        itemName = menuItem.itemName,
                        itemPrice = menuItem.itemPrice,
                        quantity = product.qtyOrder
                    )
                    cartItem.add(menu)

                    // Notify the adapter that the data has changed
                    withContext(Dispatchers.Main) {
                        holder.itemRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }

            holder.btnProcess.setOnClickListener {
                // set Order Diproses
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Peringatan")
                    .setMessage("Apa User Sudah melakukan Pembayaran?")
                    .setPositiveButton("Ya") { _, _ ->
                        // Fungsi Untuk Update "Tertunda: Pembayaran Tunai" -> SET Sukses: Pembayaran Tunai
                        viewModel.updateOrderStateByID(
                            currentOrderItem.orderId,
                            "Order Diproses",
                            "Sukses: Pembayaran tunai"
                        ).observe(lifecycleOwner) {
                            if (it.message == "Success") {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Sukses Update Order",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Failed Update Order",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                    .setNegativeButton("Tidak") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }.create().show()
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
