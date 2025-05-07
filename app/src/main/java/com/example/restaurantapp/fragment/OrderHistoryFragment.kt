package com.example.restaurantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.OrderHistoryAdapter
import com.example.restaurantapp.model.OrderHistoryDatabase
import com.example.restaurantapp.model.OrderHistoryEntity
import com.example.restaurantapp.model.OrderHistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_order_history)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        orderHistoryAdapter = OrderHistoryAdapter(emptyList())
        recyclerView.adapter = orderHistoryAdapter

        loadOrderHistory()

        return view
    }

    private fun loadOrderHistory() {
        val db = OrderHistoryDatabase.getDatabase(requireContext())
        val dao = db.orderHistoryDao()

        lifecycleScope.launch {
            try {
                val orders: List<OrderHistoryEntity> = withContext(Dispatchers.IO) { dao.getAllOrders() }

                if (orders.isNotEmpty()) {
                    val orderHistoryItems = orders.map {
                        OrderHistoryItem(
                            orderNumber = it.orderNumber,
                            itemsSummary = it.itemsSummary,
                            total = it.total,
                            readyTime = it.readyTime,
                            orderDate = it.orderDate
                        )
                    }

                    withContext(Dispatchers.Main) {
                        orderHistoryAdapter.updateData(orderHistoryItems)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}