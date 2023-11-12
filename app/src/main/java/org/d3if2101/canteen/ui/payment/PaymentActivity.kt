package org.d3if2101.canteen.ui.payment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerSavedCardsAdapter
import org.d3if2101.canteen.datamodels.SavedCardItem

class PaymentActivity : AppCompatActivity(), RecyclerSavedCardsAdapter.OnItemClickListener {

    private lateinit var totalPaymentTV: TextView
    private lateinit var confirmPaymentBtn: Button
    private lateinit var cashPaymentRB: RadioButton
    private lateinit var qrisPaymentRB: RadioButton  // Added for QRIS payment

    private var totalItemPrice = 0
    private var takeAwayTime = ""

    private var selectedSavedCard = ""

    private lateinit var savedCardsRecyclerAdapter: RecyclerSavedCardsAdapter
    private val savedCardItems = ArrayList<SavedCardItem>()

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.baseline_warning_amber_24)
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin batalkan pembayaran?")
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
        setContentView(R.layout.activity_payment)

        totalItemPrice = intent.getIntExtra("totalItemPrice", 0)
        takeAwayTime = intent?.getStringExtra("takeAwayTime").toString()

        totalPaymentTV = findViewById(R.id.total_payment_tv)
        totalPaymentTV.text = this.getString(R.string.rupiah, totalItemPrice)

        cashPaymentRB = findViewById(R.id.cash_payment_radio_btn)
        qrisPaymentRB = findViewById(R.id.qris_payment_radio_btn)

        setupPaymentButtons()

        findViewById<ImageView>(R.id.payment_go_back_iv).setOnClickListener { onBackPressed() }
    }


    private fun setupPaymentButtons() {
        confirmPaymentBtn = findViewById(R.id.confirm_payment_btn)

        confirmPaymentBtn.setOnClickListener { orderDone() }
    }


    fun chooseModeOfPayment(view: View) {
        var payMethod = ""
        payMethod = if (view is RadioButton) {
            ((view.parent as LinearLayout).getChildAt(1) as TextView).text.toString()
        } else {
            (((view as CardView).getChildAt(0) as LinearLayout).getChildAt(1) as TextView).text.toString()
        }

        cashPaymentRB.isChecked = false
        qrisPaymentRB.isChecked = false

        confirmPaymentBtn.visibility = ViewGroup.INVISIBLE

        when (payMethod) {
            getString(R.string.cash_payment) -> {
                cashPaymentRB.isChecked = true
                confirmPaymentBtn.visibility = ViewGroup.VISIBLE
            }

            getString(R.string.qris_payment) -> {
                qrisPaymentRB.isChecked = true
                confirmPaymentBtn.visibility = ViewGroup.VISIBLE
            }
        }
    }

    private fun orderDone() {
        var paymentMethod = ""
        var method = ""
        when {
            cashPaymentRB.isChecked -> {
                paymentMethod = "Tertunda: Pembayaran Tunai"
                method = "tunai"
                val intent = Intent(this, OrderDoneActivity::class.java)
                intent.putExtra("totalItemPrice", totalItemPrice)
                intent.putExtra("takeAwayTime", takeAwayTime)
                intent.putExtra("paymentMethod", paymentMethod)
                intent.putExtra("method", method)

                startActivity(intent)
                finish()
            }

            qrisPaymentRB.isChecked -> {
                paymentMethod = "null"
                method = "qris"
                val intent = Intent(this, ScanQrActivity::class.java)
                intent.putExtra("totalItemPrice", totalItemPrice)
                intent.putExtra("takeAwayTime", takeAwayTime)
                intent.putExtra("paymentMethod", paymentMethod)
                intent.putExtra("method", method)

                startActivity(intent)
                finish()
            }
        }


    }

    override fun onItemClick(position: Int) {
        for (i in 0 until savedCardItems.size) {
            savedCardItems[i].isSelected = false
        }
        savedCardItems[position].isSelected = true
        savedCardsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onItemPayButtonClick(position: Int) {
        selectedSavedCard = "XXXX${
            savedCardItems[position].cardNumber.substring(
                12,
                16
            )
        }, ${savedCardItems[position].cardHolderName}"
        orderDone()
    }
}
