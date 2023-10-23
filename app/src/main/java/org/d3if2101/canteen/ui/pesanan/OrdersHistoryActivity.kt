package org.d3if2101.canteen.ui.pesanan

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerOrderHistoryAdapter
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory

class OrdersHistoryActivity : AppCompatActivity() {

    private var orderHistoryList = ArrayList<OrderHistoryItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerOrderHistoryAdapter
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this)
    }
    private val viewModel: OrderViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_history)

        recyclerView = findViewById(R.id.order_history_recycler_view)
        recyclerAdapter = RecyclerOrderHistoryAdapter(this, orderHistoryList)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadOrderHistoryListFromDatabase()
    }

    private fun loadOrderHistoryListFromDatabase() {
        viewModel.getFirebaseAuthUID().observe(this) { uid ->
            viewModel.getOrderRecord().observe(this) { orderHistoryItems ->
                val pesananSelesai = orderHistoryItems.filter {
                    it.buyerUid == uid && it.orderStatus.lowercase().contains("selesai")
                }.sortedByDescending { it.time }
                Log.d(TAG, "loadOrderHistoryListFromDatabase: $pesananSelesai")
                orderHistoryList.addAll(pesananSelesai)
                recyclerAdapter.notifyItemRangeInserted(0, pesananSelesai.size)

                if (pesananSelesai.isNotEmpty()) {
                    findViewById<LinearLayout>(R.id.order_history_empty_indicator_ll).visibility =
                        ViewGroup.GONE
                }
            }
        }
    }

    fun goBack(view: View) {
        onBackPressed()
    }

    companion object {
        const val TAG = "OrdersHistory"
    }
}