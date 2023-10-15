package org.d3if2101.canteen.ui.payment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityOrderDoneBinding
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.CurrentOrderItem
import org.d3if2101.canteen.datamodels.OrderDetail
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.menu.MenuActivity
import org.d3if2101.canteen.ui.pesanan.OrderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderDoneActivity : AppCompatActivity() {

    private lateinit var completeLL: LinearLayout
    private lateinit var processingLL: LinearLayout
    private lateinit var databaseHandler: DatabaseHandler

    private lateinit var orderStatusTV: TextView
    private lateinit var orderIDTV: TextView
    private lateinit var dateAndTimeTV: TextView

    private var totalItemPrice = 0
    private var totalTaxPrice = 0.0F
    private var subTotalPrice = 0.0F
    private var paymentMethod = ""
    private var takeAwayTime = ""

    private var orderID = ""
    private var orderDate = ""

    private lateinit var binding: ActivityOrderDoneBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: OrderViewModel by viewModels { factory }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MenuActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        completeLL = findViewById(R.id.order_done_complete_ll)
        processingLL = findViewById(R.id.order_done_processing_ll)

        orderStatusTV = findViewById(R.id.order_done_order_status_tv)
        orderIDTV = findViewById(R.id.order_done_order_id_tv)
        dateAndTimeTV = findViewById(R.id.order_done_date_and_time_tv)

        totalItemPrice = intent.getIntExtra("totalItemPrice", 0)
        totalTaxPrice = intent.getFloatExtra("totalTaxPrice", 0.0F)
        subTotalPrice = intent.getFloatExtra("subTotalPrice", 0.0F)

        paymentMethod = intent?.getStringExtra("paymentMethod").toString()
        takeAwayTime = intent?.getStringExtra("takeAwayTime").toString()


        findViewById<TextView>(R.id.order_done_total_amount_tv).text = this.getString(R.string.rupiah, totalItemPrice)
        findViewById<TextView>(R.id.order_done_payment_method_tv).text = paymentMethod
        findViewById<TextView>(R.id.order_done_take_away_time).text = takeAwayTime

        Handler().postDelayed({
            window.statusBarColor = resources.getColor(R.color.light_green)
            window.navigationBarColor = resources.getColor(R.color.light_green)
            processingLL.visibility = ViewGroup.INVISIBLE
            completeLL.visibility = ViewGroup.VISIBLE
        }, 2000)

        generateOrderID()
        setCurrentDateAndTime()
        saveOrderRecordToDatabase()

        findViewById<LinearLayout>(R.id.order_done_cancel_order_ll).setOnClickListener { cancelCurrentOrder() }

        databaseHandler = DatabaseHandler(this)
        databaseHandler.readOrderData()

        binding.btnBack.setOnClickListener { openMenuActivity() }
    }

    private fun generateOrderID() {
        val r1: String = ('A'..'Z').random().toString() + ('A'..'Z').random().toString()
        val r2: Int = (10000..99999).random()

        orderID = "$r1$r2"
        orderIDTV.text = "ID Pesanan: $orderID"
    }

    private fun setCurrentDateAndTime() {
        val c = Calendar.getInstance()
        val monthName = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val dayNumber = c.get(Calendar.DAY_OF_MONTH)
        val year = c.get(Calendar.YEAR)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        val time = "$hour:$minute"
        val date = SimpleDateFormat("HH:mm").parse(time)
        val orderTime = SimpleDateFormat("hh:mm aa").format(date)

        orderDate = "${monthName.substring(0, 3)} %02d, $year pukul $orderTime".format(dayNumber)
        dateAndTimeTV.text = orderDate
    }

    private fun saveOrderRecordToDatabase() {
        val db = DatabaseHandler(this)
        val data = db.readCurrentOrdersData()
        val productId = mutableListOf<OrderDetail>()

        if(data.isEmpty()) {
            return
        }

        for (i in 0 until data.size) {
            val orderDetail =  OrderDetail()
            orderDetail.productId = data[i].orderID
            orderDetail.qtyOrder = data[i].totalQuantities
            productId.add(orderDetail)
        }

        val item = OrderHistoryItem(
            date = orderDate,
            orderId = orderID,
            orderStatus = "Order Successful",
            orderPayment = paymentMethod,
            price = this.getString(R.string.rupiah, totalItemPrice),
            productIDs = productId
        )
        db.insertOrderData(item)
        viewModel.insertOrderRecord(item)

        saveCurrentOrderToDatabase()
        Log.d(TAG, "saveOrderRecordToDatabase: $item")
    }

    private fun saveCurrentOrderToDatabase() {
        val item = CurrentOrderItem(
            orderID = orderID,
            takeAwayTime = takeAwayTime,
            paymentStatus = if(paymentMethod.startsWith("Tertunda")) "Tertunda" else "Selesai",
            orderItemNames = getOrderItemNames(),
            orderItemQuantities = getOrderItemQty(),
            totalItemPrice = totalItemPrice
        )
        val db = DatabaseHandler(this)
        db.insertCurrentOrdersData(item)
        Log.d(TAG, "saveCurrentOrderToDatabase: $item")
    }


    private fun cancelCurrentOrder() {
        AlertDialog.Builder(this)
            .setTitle("Batalkan Pesanan")
            .setMessage("Apa kamu yakin membatalkan pesanan ini?")
            .setPositiveButton("Ya") { _, _ ->
                val result = DatabaseHandler(this).deleteCurrentOrderRecord(orderID)
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

    private fun openMenuActivity() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun getOrderItemNames(): String {
        //stores all the item names in a single string separated by (;)
        var itemNames = ""
        for(item in DatabaseHandler(this).readCartData()) {
            itemNames += item.itemName + ";"
        }
        return itemNames.substring(0, itemNames.length-1)
    }

//    private fun getCartItem(): CartItem {
//
//        for(item in DatabaseHandler(this).readCartData()) {
//            itemNames += item.itemName + ";"
//        }
//        return
//    }

    private fun getOrderItemQty(): String {
        //stores all the item qty in a single string separated by (;)
        var itemQty = ""
        for(item in DatabaseHandler(this).readCartData()) {
            itemQty += item.quantity.toString() + ";"
        }
        return itemQty.substring(0, itemQty.length-1)
    }

    companion object {
        const val TAG = "testo"
    }
}