package org.d3if2101.canteen.ui.penjual.order

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragment = OrderFragment()
//        fragment.arguments = Bundle().apply {
//            putInt(FollowFragment.ARG_POSITION, position + 1)
//            putString(FollowFragment.ARG_USERNAME, nickname)
//        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 3
    }
}