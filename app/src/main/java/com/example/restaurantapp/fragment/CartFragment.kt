package com.example.restaurantapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.CartAdapter
import com.example.restaurantapp.model.OrderHistoryDatabase
import com.example.restaurantapp.model.OrderHistoryEntity
import com.example.restaurantapp.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

class CartFragment : Fragment() {

    private fun clearHistory() {
        val db = OrderHistoryDatabase.getDatabase(requireContext())
        val dao = db.orderHistoryDao()
        lifecycleScope.launch {
            dao.clearAllOrders()
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var buttonOrder: Button
    private lateinit var buttonClearCart: Button
    private lateinit var buttonOrderHistory: Button
    private lateinit var textTotal: TextView
    private lateinit var textOrderSummary: TextView
    private lateinit var textPickupTime: TextView

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var lastOrderTotal: Double = 0.0 // Saving the total before clearing cart
    private var lastPickupTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*
         fun clearHistory() {
            val db = OrderHistoryDatabase.getDatabase(requireContext())
            val dao = db.orderHistoryDao()
            lifecycleScope.launch {
                dao.clearAllOrders()
            }
        }
        clearHistory()
        */

        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_cart)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        textTotal = view.findViewById(R.id.text_total)
        buttonOrder = view.findViewById(R.id.button_order)
        buttonClearCart = view.findViewById(R.id.button_clear_cart)
        buttonOrderHistory = view.findViewById(R.id.button_order_history)
        textOrderSummary = view.findViewById(R.id.text_order_summary)
        textPickupTime = view.findViewById(R.id.text_pickup_time)

        buttonOrderHistory.setOnClickListener {
            findNavController().navigate(R.id.orderHistoryFragment)
        }

        sharedViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter = CartAdapter(
                cartItems,
                onQuantityChanged = { menuItem, modifications, quantity ->
                    sharedViewModel.updateQuantity(menuItem, modifications, quantity)
                    updateTotal()
                },
                onRemoveItem = { menuItem, modifications ->
                    sharedViewModel.removeItem(menuItem, modifications)
                    updateTotal()
                }
            )
            recyclerView.adapter = cartAdapter
            updateTotal()
        }

        buttonOrder.setOnClickListener {
            placeOrder()
        }

        buttonClearCart.setOnClickListener {
            sharedViewModel.clearCart()
            updateTotal()
            clearOrderSummary()
        }

        return view
    }

    private fun placeOrder() {
        val cartItems = sharedViewModel.cartItems.value ?: emptyList()

        if (cartItems.isNotEmpty()) {
            val db = OrderHistoryDatabase.getDatabase(requireContext())
            val dao = db.orderHistoryDao()

            lastOrderTotal = sharedViewModel.getTotalPrice() // Save total before clearing cart
            lastPickupTime = calculateReadyTime()

            lifecycleScope.launch {
                val orderEntity = OrderHistoryEntity(
                    orderNumber = generateOrderNumber(),
                    itemsSummary = getItemsSummary(),
                    total = lastOrderTotal,
                    readyTime = lastPickupTime,
                    orderDate = getCurrentDate()
                )
                dao.insertOrder(orderEntity)

                Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()

                sharedViewModel.clearCart()
                showOrderSummary()
            }
        }
    }

    private fun updateTotal() {
        val total = sharedViewModel.getTotalPrice()
        textTotal.text = "Total: $%.2f".format(total)
    }

    private fun showOrderSummary() {
        textTotal.visibility = View.GONE  // 👈 ADD this line
        textOrderSummary.text = "Total: $%.2f".format(lastOrderTotal)
        textPickupTime.text = "Pickup Time: $lastPickupTime"
        textOrderSummary.visibility = View.VISIBLE
        textPickupTime.visibility = View.VISIBLE
    }

    private fun clearOrderSummary() {
        textOrderSummary.text = ""
        textPickupTime.text = ""
        textOrderSummary.visibility = View.GONE
        textPickupTime.visibility = View.GONE
    }

    private fun generateOrderNumber(): String {
        return "ORDER: " + System.currentTimeMillis()
    }

    private fun getItemsSummary(): String {
        val cartItems = sharedViewModel.cartItems.value ?: emptyList()
        return cartItems.joinToString(", ") { it.menuItem.name }
    }

    private fun calculateReadyTime(): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.MINUTE, 20)
        val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

}