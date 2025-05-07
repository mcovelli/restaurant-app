// app/src/main/java/com/example/restaurantapp/adapter/MenuAdapter.kt
package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.model.MenuItem

class MenuAdapter(
    private var menuItems: List<MenuItem>,
    private val onItemClicked: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.menu_item_image)
        val textName: TextView = itemView.findViewById(R.id.menu_item_name)
        val textDescription: TextView = itemView.findViewById(R.id.menu_item_description)
        val textPrice: TextView = itemView.findViewById(R.id.menu_item_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuItems[position]
        holder.image.setImageResource(item.imageResId)
        holder.textName.text = item.name
        holder.textDescription.text = item.description
        holder.textPrice.text = "$%.2f".format(item.price)
        holder.itemView.setOnClickListener { onItemClicked(item) }
    }

    override fun getItemCount(): Int = menuItems.size

    fun updateList(newItems: List<MenuItem>) {
        menuItems = newItems
        notifyDataSetChanged()
    }
}