package org.d3if2101.canteen.ui.pesanan

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerOrderHistoryAdapter
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel

class OrdersHistoryActivity : AppCompatActivity() {

    private var orderHistoryList = ArrayList<OrderHistoryItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerOrderHistoryAdapter

    private lateinit var deleteRecordsIV : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_history)

        recyclerView = findViewById(R.id.order_history_recycler_view)
        recyclerAdapter = RecyclerOrderHistoryAdapter(this, orderHistoryList)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        deleteRecordsIV = findViewById(R.id.order_history_delete_records_iv)
        deleteRecordsIV.setOnClickListener { deleteOrderHistoryRecords() }

        loadOrderHistoryListFromDatabase()
    }

    private fun loadOrderHistoryListFromDatabase() {
        val db = DatabaseHandler(this)
        val data = db.readOrderData()

        if(data.size == 0) {
            deleteRecordsIV.visibility = ViewGroup.INVISIBLE
            return
        }

        findViewById<LinearLayout>(R.id.order_history_empty_indicator_ll).visibility = ViewGroup.GONE
        for(i in 0 until data.size) {
            val item = OrderHistoryItem()
            item.date = data[i].date
            item.orderId = data[i].orderId
            item.orderStatus = data[i].orderStatus
            item.orderPayment = data[i].orderPayment
            item.price = data[i].price
            orderHistoryList.add(item)
            orderHistoryList.reverse()
            recyclerAdapter.notifyItemRangeInserted(0, data.size)
        }
    }

    private fun deleteOrderHistoryRecords() {
        AlertDialog.Builder(this)
            .setMessage("Apa kamu yakin hapus semua riwayat pesanan?")
            .setPositiveButton("Ya") { dialogInterface, _ ->
                val db = DatabaseHandler(this)
                db.dropOrderHistoryTable()
                deleteRecordsIV.visibility = ViewGroup.INVISIBLE
                findViewById<LinearLayout>(R.id.order_history_empty_indicator_ll).visibility =
                    ViewGroup.VISIBLE


                val size = orderHistoryList.size
                orderHistoryList.clear()
                recyclerAdapter.notifyItemRangeRemoved(0, size)

                dialogInterface.dismiss()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

    fun goBack(view: View) {onBackPressed()}
}