package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.model.OrderHistoryItem

class OrderHistoryAdapter(
    private var orders: List<OrderHistoryItem>
) : RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = orders[position]
        holder.orderNumberText.text = order.orderNumber
        holder.itemsSummaryText.text = order.itemsSummary
        holder.totalText.text = "$%.2f".format(order.total)
        holder.readyTimeText.text = order.readyTime
        holder.orderDateText.text = order.orderDate
    }

    override fun getItemCount(): Int = orders.size

    fun updateData(newOrders: List<OrderHistoryItem>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderNumberText: TextView = itemView.findViewById(R.id.text_order_number)
        val itemsSummaryText: TextView = itemView.findViewById(R.id.text_items_summary)
        val totalText: TextView = itemView.findViewById(R.id.text_total)
        val readyTimeText: TextView = itemView.findViewById(R.id.text_ready_time)
        val orderDateText: TextView = itemView.findViewById(R.id.text_order_date)
    }
}