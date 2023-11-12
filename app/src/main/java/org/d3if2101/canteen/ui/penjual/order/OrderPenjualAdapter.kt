package org.d3if2101.canteen.ui.penjual.order

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.d3if2101.canteen.ui.penjual.order.ambilpesanan.AmbilPesananFragment
import org.d3if2101.canteen.ui.penjual.order.order.OrderFragment
import org.d3if2101.canteen.ui.penjual.order.proses.ProsesFragment
import org.d3if2101.canteen.ui.penjual.order.selesai.SelesaiFragment

class OrderPenjualAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderFragment()
            1 -> ProsesFragment()
            2 -> SelesaiFragment()
            3 -> AmbilPesananFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


    override fun getItemCount(): Int {
        return 4
    }

    companion object {
        const val ARG_POSITION = "ARG_POSITION"
    }
}