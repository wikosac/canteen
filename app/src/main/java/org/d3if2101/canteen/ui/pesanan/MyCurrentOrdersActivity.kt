package org.d3if2101.canteen.ui.pesanan

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerCurrentOrderAdapter
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.utils.convertStringToDate

class MyCurrentOrdersActivity : AppCompatActivity(),
    RecyclerCurrentOrderAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerCurrentOrderAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val viewModel: OrderViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_current_orders)

        var orderRecords: List<OrderHistoryItem>?
        viewModel.getOrderRecord().observe(this@MyCurrentOrdersActivity) { orders ->
            if (orders != null) {
                // Mengonversi string tanggal ke objek Date
                val orderRecords = orders.map { order ->
                    val dateStr = order.date // Gantilah ini dengan cara Anda mengakses tanggal di objek Order
                    val date = convertStringToDate(dateStr)
                    Pair(order, date)
                }

                // Mengurutkan berdasarkan tanggal secara descending
                val sortedRecords = orderRecords.sortedByDescending { it.second }

                val sortedOrders = sortedRecords.map { it.first }

                // Kemudian Anda bisa menggabungkannya ke dalam adapter
                lifecycleScope.launch {
                    recyclerAdapter = RecyclerCurrentOrderAdapter(
                        context = this@MyCurrentOrdersActivity,
                        currentOrderList = sortedOrders,
                        viewModel = viewModel,
                        lifecycleOwner = this@MyCurrentOrdersActivity,
                        listener = this@MyCurrentOrdersActivity
                    )

                    recyclerView.adapter = recyclerAdapter
                }
            }
        }


        recyclerView = findViewById(R.id.current_order_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun cancelOrder(orderId: String) {
        AlertDialog.Builder(this)
            .setTitle("Batalkan pesanan")
            .setMessage("Apa kamu yakin batalkan pesanan ini?")
            .setPositiveButton("Ya") { dialogInterface, _ ->
                viewModel.deleteOrderByID(orderId).observe(this) {
                    if (it.message.lowercase() == "Success") {
                        Toast.makeText(this, "Berhasil Membatalkan pesan", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Berhasil Membatalkan pesan", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

    fun goBack(view: View) {
        onBackPressed()
    }

    companion object {
        const val TAG = "testo"
    }
}