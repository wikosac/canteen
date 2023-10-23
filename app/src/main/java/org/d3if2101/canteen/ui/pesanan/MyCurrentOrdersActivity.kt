package org.d3if2101.canteen.ui.pesanan

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerCurrentOrderAdapter
import org.d3if2101.canteen.ui.ViewModelFactory

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

        recyclerView = findViewById(R.id.current_order_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getOrderRecord().observe(this@MyCurrentOrdersActivity) { orders ->
            val currentOrders = orders.filter {
                !it.orderStatus.lowercase().contains("selesai")
            }.sortedByDescending { it.time }
            if (currentOrders.isNotEmpty()) {
                findViewById<LinearLayout>(R.id.current_order_empty_indicator_ll).visibility =
                    ViewGroup.GONE

            }
            if (currentOrders != null) {
                // Kemudian Anda bisa menggabungkannya ke dalam adapter
                lifecycleScope.launch {
                    recyclerAdapter = RecyclerCurrentOrderAdapter(
                        context = this@MyCurrentOrdersActivity,
                        currentOrderList = currentOrders,
                        viewModel = viewModel,
                        lifecycleOwner = this@MyCurrentOrdersActivity,
                        listener = this@MyCurrentOrdersActivity
                    )

                    // Kemudian Anda bisa menggabungkannya ke dalam adapter
                    lifecycleScope.launch {
                        recyclerAdapter = RecyclerCurrentOrderAdapter(
                            context = this@MyCurrentOrdersActivity,
                            currentOrderList = currentOrders,
                            viewModel = viewModel,
                            lifecycleOwner = this@MyCurrentOrdersActivity,
                            listener = this@MyCurrentOrdersActivity
                        )

                        recyclerView.adapter = recyclerAdapter
                    }
                }
            }
        }
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
        const val TAG = "CurrentOrders"
    }
}