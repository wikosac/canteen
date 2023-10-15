package org.d3if2101.canteen.ui.penjual.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderPenjualAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragment = OrderFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position + 1)
//            putString(FollowFragment.ARG_USERNAME, nickname)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }

    companion object {
        const val ARG_POSITION = "ARG_POSITION"
    }
}