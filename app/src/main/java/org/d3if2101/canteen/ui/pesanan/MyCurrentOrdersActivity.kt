package org.d3if2101.canteen.ui.pesanan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerCurrentOrderAdapter
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.payment.QRCodeFragment

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
        viewModel.getOrderRecord().observe(this@MyCurrentOrdersActivity) {
            Log.d(TAG, "onCreate order record: $it")
            orderRecords = it
            orderRecords?.let { records ->
                recyclerAdapter = RecyclerCurrentOrderAdapter(
                    context = this,
                    currentOrderList = records,
                    viewModel = viewModel,
                    lifecycleOwner = this,
                    listener = this
                )
                recyclerView.adapter = recyclerAdapter
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
                viewModel.deleteOrderByID(orderId)
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