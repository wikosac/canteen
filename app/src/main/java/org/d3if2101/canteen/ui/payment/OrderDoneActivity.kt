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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityOrderDoneBinding
import org.d3if2101.canteen.datamodels.CurrentOrderItem
import org.d3if2101.canteen.datamodels.OrderDetail
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
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
    private var orderRecordSaved = false

    private var totalItemPrice = 0
    private var totalTaxPrice = 0.0F
    private var subTotalPrice = 0.0F
    private var paymentMethod = ""
    private var takeAwayTime = ""

    private var orderID = ""
    private var orderDate = ""
    private var timeInMillis: Long = 0
    private var userUID: String = ""
    private var method: String = ""

    private lateinit var binding: ActivityOrderDoneBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: OrderViewModel by viewModels { factory }

    override fun onBackPressed() {
        super.onBackPressed()
        val db = DatabaseHandler(this)
        db.deleteAllCurrentOrders()
        val intent = Intent(this, DashboardActivity::class.java)
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
        method = intent?.getStringExtra("method").toString()



        findViewById<TextView>(R.id.order_done_total_amount_tv).text =
            this.getString(R.string.rupiah, totalItemPrice)
        findViewById<TextView>(R.id.order_done_payment_method_tv).text = paymentMethod
        findViewById<TextView>(R.id.order_done_take_away_time).text = takeAwayTime

        Handler().postDelayed({
            window.statusBarColor = resources.getColor(R.color.light_green)
            window.navigationBarColor = resources.getColor(R.color.light_green)
            processingLL.visibility = ViewGroup.INVISIBLE
            completeLL.visibility = ViewGroup.VISIBLE
        }, 2000)

        viewModel.getFirebaseAuthUID().observe(this){
            userUID = it.toString()
            if (!orderRecordSaved) {
                GlobalScope.launch {
                    saveOrderRecordToDatabase() // save to Firebase
                }
                orderRecordSaved = true // Set the flag to indicate that it has been executed.
            }
        }

        generateOrderID()
        setCurrentDateAndTime()
        saveCurrentOrderToDatabase() // save Current Order

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
        val orderTime = SimpleDateFormat("HH:mm").format(date)

        timeInMillis = c.timeInMillis
//        Log.d(TAG, "setCurrentDateAndTime: ${c.timeInMillis}")

        orderDate = "%02d ${monthName.substring(0, 3)} $year, $orderTime".format(dayNumber)
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
            orderDetail.productId = data[i].idProduct
            orderDetail.qtyOrder = data[i].orderItemQuantities.toInt()
            productId.add(orderDetail)
        }
        var orderStatus = ""
        val totalQty = productId.sumOf { it.qtyOrder }
        if(method.equals("qris")){
            orderStatus = "Order Diproses"
        } else {
            orderStatus = "Order Successful"
        }
        data.forEach { data ->
            val itemInsertToFirebase = OrderHistoryItem(
                buyerUid =  userUID,
                date = orderDate,
                time = timeInMillis,
                orderId = orderID,
                orderStatus = orderStatus,
                orderPayment = paymentMethod,
                quantity = totalQty,
                price = this.getString(R.string.rupiah, totalItemPrice),
                sellerUid = data.idPenjual,
                productIDs = productId,
                methodPayment = method
            )
            viewModel.insertOrderRecord(itemInsertToFirebase)
            val item = OrderHistoryItem(
                date = orderDate,
                orderId = orderID,
                orderStatus = orderStatus,
                orderPayment = paymentMethod,
                price = this.getString(R.string.rupiah, totalItemPrice),
                productIDs = productId
            )

            db.insertOrderData(item)
            Log.d(TAG, "saveOrderRecordToDatabase: $item")
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val db = DatabaseHandler(this)
        db.deleteAllCurrentOrders()
    }

    private fun saveCurrentOrderToDatabase() {
        val db = DatabaseHandler(this)
        val dbCurrentCart = db.readCartData()

        dbCurrentCart.forEach { dataCustomer ->
            Log.d(TAG, dataCustomer.itemName + "testCurrentOrder")

            val item = CurrentOrderItem(
                orderID = orderID,
                takeAwayTime = takeAwayTime,
                paymentStatus = if (paymentMethod.startsWith("Tertunda")) "Tertunda" else "Selesai",
                orderItemNames = dataCustomer.itemName,
                orderItemQuantities = dataCustomer.quantity.toString(),
                totalItemPrice = totalItemPrice,
                idPenjual = dataCustomer.idPenjual,
                idProduct = dataCustomer.itemID
            )
            val db2 = DatabaseHandler(this)
            db2.insertCurrentOrdersData(item)
            Log.d(TAG, "saveCurrentOrderToDatabase: $item")
        }
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
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    companion object {
        const val TAG = "OrderDone"
    }
}