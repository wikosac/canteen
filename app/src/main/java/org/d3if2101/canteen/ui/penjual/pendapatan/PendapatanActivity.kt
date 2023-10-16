package org.d3if2101.canteen.ui.penjual.pendapatan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if2101.canteen.databinding.ActivityPendapatanBinding
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.pesanan.OrderViewModel
import java.text.NumberFormat
import java.util.Locale

class PendapatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPendapatanBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private var totalPendapatan: Int = 0
    private val viewModel: OrderViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPendapatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))


        viewModel.getPendapatan().observe(this) {
            it.forEach { order ->
                val price = order.price.replace("Rp ", "").replace(",", "")?.toIntOrNull() ?: 0
                totalPendapatan += price
            }
            setItemData(it)

            binding.txtPendapatannya.text =   "Total Pendapatan: ${numberFormat.format(totalPendapatan)}"
        }
        binding.rv.layoutManager = LinearLayoutManager(this)

    }

    private fun setItemData(itemsItem: List<OrderHistoryItem>) {
        val adapter = PendapatanAdapter(itemsItem)
        binding.rv.adapter = adapter
    }

}