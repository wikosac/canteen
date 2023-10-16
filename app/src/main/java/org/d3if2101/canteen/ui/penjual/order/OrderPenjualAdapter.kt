package org.d3if2101.canteen.ui.penjual.order

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderPenjualAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OrderFragment()
            1 -> ProsesFragment()
            2 -> SelesaiFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


    override fun getItemCount(): Int {
        return 3
    }

    companion object {
        const val ARG_POSITION = "ARG_POSITION"
    }
}