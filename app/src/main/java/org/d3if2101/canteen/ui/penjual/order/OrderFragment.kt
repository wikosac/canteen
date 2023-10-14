package org.d3if2101.canteen.ui.penjual.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if2101.canteen.databinding.FragmentOrderBinding
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.pesanan.OrderViewModel

class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private var position: Int = 0

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val viewModel: OrderViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        binding.rvOrderItem.layoutManager = LinearLayoutManager(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        viewModel.getOrderRecord().observe(viewLifecycleOwner) {
            setItemData(it)
        }
    }

    private fun setItemData(itemsItem: List<OrderHistoryItem>) {
        val adapter = OrderItemAdapter(itemsItem)
        binding.rvOrderItem.adapter = adapter
    }

    companion object {
        const val ARG_POSITION = "section_number"
    }
}
