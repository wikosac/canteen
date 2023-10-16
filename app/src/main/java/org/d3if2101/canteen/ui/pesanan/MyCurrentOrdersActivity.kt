package org.d3if2101.canteen.ui.pesanan

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerCurrentOrderAdapter
import org.d3if2101.canteen.datamodels.CurrentOrderItem
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.services.DatabaseHandler
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

        val currentOrderItems: ArrayList<CurrentOrderItem> = ArrayList()
        val orderCurrent: List<CurrentOrderItem> = listOf()

        viewModel.getOrderRecord().observe(this@MyCurrentOrdersActivity) {
            currentOrderItems.addAll(orderCurrent.map { it })
            recyclerView = findViewById(R.id.current_order_recycler_view)
            recyclerAdapter = RecyclerCurrentOrderAdapter(
                this, currentOrderList = orderCurrent, this
            )
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        currentOrderItems.forEach{ a ->
            Log.d(TAG, "onCreate orderCurrentItem: ${a.orderItemNames}")
        }

    }

    private fun loadCurrentOrdersFromDatabase() {

        val db = DatabaseHandler(this)
        val data = db.readCurrentOrdersData()
        Log.d(TAG, "loadCurrentOrdersFromDatabase: $data")

        if (data.isEmpty()) {
            return
        }

        for (i in 0 until data.size) {
            val currentOrderItem = CurrentOrderItem()

            currentOrderItem.orderID = data[i].orderID
            currentOrderItem.takeAwayTime = data[i].takeAwayTime
            currentOrderItem.paymentStatus = data[i].paymentStatus
            currentOrderItem.orderItemNames = data[i].orderItemNames
            currentOrderItem.orderItemQuantities = data[i].orderItemQuantities
            currentOrderItem.totalItemPrice = data[i].totalItemPrice
            currentOrderItem.tax = data[i].tax
            currentOrderItem.subTotal = data[i].subTotal

//            currentOrderList.add(currentOrderItem)
//            currentOrderList.reverse()
            recyclerAdapter.notifyItemRangeInserted(0, data.size)
        }

        findViewById<LinearLayout>(R.id.current_order_empty_indicator_ll).visibility =
            ViewGroup.GONE
    }

    override fun showQRCode(orderID: String) {
        //User have to just show the QR Code, and canteen staff have to scan, so user don't have to wait more
        val bundle = Bundle()
        bundle.putString("orderID", orderID)

        val dialog = QRCodeFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "QR Code Generator")
    }

    override fun cancelOrder(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Batalkan pesanan")
            .setMessage("Apa kamu yakin batalkan pesanan ini?")
            .setPositiveButton("Ya") { dialogInterface, _ ->
//                val result =
//                    DatabaseHandler(this).deleteCurrentOrderRecord(orderList[position].orderId)
//                orderList.removeAt(position)
//                recyclerAdapter.notifyItemRemoved(position)
//                recyclerAdapter.notifyItemRangeChanged(position, orderList.size)
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
//
//                if (orderList.isEmpty()) {
//                    findViewById<LinearLayout>(R.id.current_order_empty_indicator_ll).visibility =
//                        ViewGroup.VISIBLE
//                }

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