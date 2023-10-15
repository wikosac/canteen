package org.d3if2101.canteen.ui.payment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerOrderItemAdapter
import org.d3if2101.canteen.databinding.ActivityUserMenuOrderBinding
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.ui.menu.MenuActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class UserMenuOrderActivity : AppCompatActivity(),
    RecyclerOrderItemAdapter.OnItemClickListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerOrderItemAdapter

    private lateinit var totalItemsTV: TextView
    private lateinit var totalPriceTV: TextView
    private lateinit var proceedToPayBtn: Button
    private lateinit var orderTakeAwayTV: TextView

    private var totalPrice: Int = 0
    private var totalItems: Int = 0
    private val db = DatabaseHandler(this)

    private lateinit var binding: ActivityUserMenuOrderBinding

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.baseline_warning_amber_24)
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin batalkan pesanan?")
            .setPositiveButton("Ya") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMenuOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        totalItemsTV = findViewById(R.id.order_total_items_tv)
        totalPriceTV = findViewById(R.id.order_total_price_tv)
        proceedToPayBtn = findViewById(R.id.proceed_to_pay_btn)
        orderTakeAwayTV = findViewById(R.id.order_take_away_time_tv)

        totalPrice = intent.getIntExtra("totalPrice", 0)
        totalItems = intent.getIntExtra("totalItems", 0)

        loadOrderDetails()
        loadRecyclerAdapter()

        val c = Calendar.getInstance()
        c.add(Calendar.MINUTE, 5)
        onTimeSet(TimePicker(this), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE))

        binding.proceedToPayBtn.setOnClickListener { openPaymentActivity() }
    }

    private fun loadOrderDetails() {
        totalItemsTV.text = this.getString(R.string.jumlah_produk, totalItems)
        totalPriceTV.text = this.getString(R.string.rupiah, totalPrice)
        proceedToPayBtn.text = this.getString(R.string.rupiah, totalPrice)
    }

    private fun loadRecyclerAdapter() {
        val sharedPref: SharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val orderedItems: ArrayList<CartItem> = ArrayList()

        for (item in DatabaseHandler(this).readCartData()) {
            orderedItems.add(item)
        }

        itemRecyclerView = findViewById(R.id.order_recycler_view)
        recyclerAdapter = RecyclerOrderItemAdapter(
            applicationContext,
            orderedItems,
            totalItemsTV,
            totalItems,
            totalPriceTV,
            totalPrice,
            proceedToPayBtn,
            sharedPref.getInt("loadItemImages", 0),
            this
        )

        itemRecyclerView.adapter = recyclerAdapter
        itemRecyclerView.layoutManager = LinearLayoutManager(this)

        recyclerAdapter.notifyItemRangeInserted(0, orderedItems.size)
    }

    fun changeOrderTakeAwayTime(view: View) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        val time = "$hourOfDay:$minute"
        val f24Hours = SimpleDateFormat("HH:mm")
        try {
            val date = f24Hours.parse(time)
            val f12Hours = SimpleDateFormat("hh:mm aa")
            orderTakeAwayTV.text = f12Hours.format(date)
        } catch (e: Exception) {}
    }

    fun goBack(view: View) {onBackPressed()}

    override fun emptyOrder() {
        AlertDialog.Builder(this)
            .setMessage("Pesananmu masih kosong. Tambahkan beberapa menu.")
            .setPositiveButton("Ok") { _, _ ->
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            }
            .setCancelable(false)
            .create().show()
    }

    private fun openPaymentActivity() {
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("totalItemPrice", recyclerAdapter.getTotalPrice())
        intent.putExtra("takeAwayTime", orderTakeAwayTV.text.toString())
        startActivity(intent)
    }

    override fun onPlusBtnClick(item: CartItem) {
        item.quantity += 1
        db.insertCartItem (
            CartItem(
                itemID = item.itemID,
                itemName = item.itemName,
                imageUrl = item.imageUrl,
                itemPrice = item.itemPrice,
                quantity = item.quantity,
                itemStars = item.itemStars,
                itemShortDesc = item.itemShortDesc
            )
        )
    }

    override fun onMinusBtnClick(item: CartItem) {
        if (item.quantity == 0) return
        item.quantity -= 1

        val cartItem = CartItem(
            itemID = item.itemID,
            itemName = item.itemName,
            imageUrl = item.imageUrl,
            itemPrice = item.itemPrice,
            quantity = item.quantity,
            itemStars = item.itemStars,
            itemShortDesc = item.itemShortDesc
        )

        if (item.quantity == 0) {
            db.deleteCartItem(cartItem)
        } else {
            db.insertCartItem(cartItem) // Update
        }
    }
}