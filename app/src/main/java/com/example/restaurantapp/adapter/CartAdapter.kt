package com.example.restaurantapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.model.CartItem
import com.example.restaurantapp.model.MenuItem
import com.example.restaurantapp.model.Modification
import java.util.Locale

class CartAdapter(
    private val cartItems: List<CartItem>,
    // Callback to notify when quantity is changed
    private val onQuantityChanged: (menuItem: MenuItem, modifications: List<Modification>, quantity: Int) -> Unit,
    // Callback to notify when an item should be removed
    private val onRemoveItem: (menuItem: MenuItem, modifications: List<Modification>) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView     = itemView.findViewById(R.id.cart_item_name)
        val textPrice: TextView    = itemView.findViewById(R.id.cart_item_price)
        val spinnerQuantity: Spinner = itemView.findViewById(R.id.spinner_quantity)
        val buttonRemove: Button    = itemView.findViewById(R.id.button_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]

        // Build item name + mods text
        val modsText = if (cartItem.modifications.isNotEmpty()) {
            " (" + cartItem.modifications.joinToString(", ") { it.name } + ")"
        } else {
            ""
        }
        holder.textName.text = cartItem.menuItem.name + modsText

        // Calculate effective unit price (base + mods)
        val effectivePrice = cartItem.menuItem.price +
                cartItem.modifications.sumOf { it.extraCost }

        // Format to 2 decimal places
        val formattedPrice = String.format(
            Locale.getDefault(),
            "%.2f",
            effectivePrice
        )

        holder.textPrice.text = "$$formattedPrice"

        // Quantity spinner setup
        val quantities = (1..10).toList()
        val spinnerAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            quantities
        )
        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        holder.spinnerQuantity.adapter = spinnerAdapter

        // Select current quantity
        val currentQuantity = cartItem.quantity.coerceIn(1, 10)
        holder.spinnerQuantity.setSelection(currentQuantity - 1)

        // Notify on quantity change
        holder.spinnerQuantity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?,
                    pos: Int, id: Long
                ) {
                    val newQty = quantities[pos]
                    if (newQty != cartItem.quantity) {
                        onQuantityChanged(
                            cartItem.menuItem,
                            cartItem.modifications,
                            newQty
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) { /* no-op */ }
            }

        // Remove button
        holder.buttonRemove.setOnClickListener {
            onRemoveItem(cartItem.menuItem, cartItem.modifications)
        }
    }

    override fun getItemCount(): Int = cartItems.size
}