package org.d3if2101.canteen.ui.menu

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.d3if2101.canteen.R
import org.d3if2101.canteen.ui.payment.UserMenuOrderActivity

open class BottomSheetSelectedItemDialog: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.selected_menu_bottom_dialog, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalPrice = this.arguments?.getInt("totalPrice")
        val totalItems = this.arguments?.getInt("totalItems")

        view.findViewById<TextView>(R.id.total_ordered_items_price_tv).text =
            requireContext().getString(R.string.rupiah, totalPrice)
        view.findViewById<TextView>(R.id.total_ordered_items_tv).text =
            requireContext().getString(R.string.total_pesanan_kamu, totalItems)

        val placeOrderBTN: Button = view.findViewById(R.id.place_order_btn)
        placeOrderBTN.setOnClickListener {
            if(totalItems != 0) {
                val intent = Intent(context, UserMenuOrderActivity::class.java)
                intent.putExtra("totalItems", totalItems)
                intent.putExtra("totalPrice", totalPrice)

                dismiss()
                startActivity(intent)
            } else {
                Toast.makeText(context, "Please select at-least 1 item", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
