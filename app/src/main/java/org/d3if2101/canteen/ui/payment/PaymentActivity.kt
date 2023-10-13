package org.d3if2101.canteen.ui.payment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerSavedCardsAdapter
import org.d3if2101.canteen.datamodels.SavedCardItem
import org.d3if2101.canteen.services.DatabaseHandler

class PaymentActivity : AppCompatActivity(), RecyclerSavedCardsAdapter.OnItemClickListener {

    private lateinit var totalPaymentTV: TextView
    private lateinit var confirmPaymentBtn: Button

    private lateinit var cashPaymentRB: RadioButton


    var totalItemPrice = 0.0F
    var totalTaxPrice = 0.0F
    var subTotalPrice = 0.0F

    var takeAwayTime = ""

    private var selectedSavedCard = ""

    private lateinit var savedCardRecyclerView: RecyclerView
    private lateinit var savedCardsRecyclerAdapter: RecyclerSavedCardsAdapter
    private val savedCardItems = ArrayList<SavedCardItem>()

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.baseline_warning_amber_24)
            .setTitle("Alert!")
            .setMessage("Do you want to cancel the payment?")
            .setPositiveButton("Yes") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        totalItemPrice = intent.getFloatExtra("totalItemPrice", 0.0F)
        totalTaxPrice = intent.getFloatExtra("totalTaxPrice", 0.0F)
        subTotalPrice = intent.getFloatExtra("subTotalPrice", 0.0F)

        takeAwayTime = intent?.getStringExtra("takeAwayTime").toString()

        totalPaymentTV = findViewById(R.id.total_payment_tv)
        totalPaymentTV.text = "\$%.2f".format(subTotalPrice)

        cashPaymentRB = findViewById(R.id.cash_payment_radio_btn)

        setupPaymentButtons()

        findViewById<ImageView>(R.id.payment_go_back_iv).setOnClickListener { onBackPressed() }
    }


    private fun setupPaymentButtons() {
        confirmPaymentBtn = findViewById(R.id.confirm_payment_btn)

        confirmPaymentBtn.setOnClickListener { orderDone() }
    }


    fun chooseModeOfPayment(view: View) {
        var payMethod = ""
        payMethod = if(view is RadioButton) {
            ((view.parent as LinearLayout).getChildAt(1) as TextView).text.toString()
        } else {
            (((view as CardView).getChildAt(0) as LinearLayout).getChildAt(1) as TextView).text.toString()
        }

        cashPaymentRB.isChecked = false
        savedCardRecyclerView.visibility = ViewGroup.GONE

        confirmPaymentBtn.visibility = ViewGroup.INVISIBLE

        when(payMethod) {
            getString(R.string.cash_payment) -> {
                cashPaymentRB.isChecked = true
                confirmPaymentBtn.visibility = ViewGroup.VISIBLE
            }
        }
    }

    private fun orderDone() {
        var paymentMethod = ""
        when {
            cashPaymentRB.isChecked -> paymentMethod = "Pending: Cash Payment"
        }

        val intent = Intent(this, OrderDoneActivity::class.java)
        intent.putExtra("totalItemPrice", totalItemPrice)
        intent.putExtra("totalTaxPrice", totalTaxPrice)
        intent.putExtra("subTotalPrice", subTotalPrice)
        intent.putExtra("takeAwayTime", takeAwayTime)
        intent.putExtra("paymentMethod", paymentMethod)

        startActivity(intent)
        finish()
    }

    override fun onItemClick(position: Int) {
        for(i in 0 until savedCardItems.size) {
            savedCardItems[i].isSelected = false
        }
        savedCardItems[position].isSelected = true
        savedCardsRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onItemPayButtonClick(position: Int) {
        selectedSavedCard = "XXXX${savedCardItems[position].cardNumber.substring(12,16)}, ${savedCardItems[position].cardHolderName}"
        orderDone()
    }
}
