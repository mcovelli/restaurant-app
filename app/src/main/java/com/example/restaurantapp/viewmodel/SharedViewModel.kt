package com.example.restaurantapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantapp.model.CartItem
import com.example.restaurantapp.model.MenuItem
import com.example.restaurantapp.model.Modification

class SharedViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun addItem(item: MenuItem, modifications: List<Modification>, quantity: Int) {
        val existing = _cartItems.value?.find { it.menuItem == item && it.modifications == modifications }
        if (existing != null) {
            existing.quantity += quantity
            _cartItems.value = _cartItems.value  // trigger observers
        } else {
            _cartItems.value = (_cartItems.value ?: emptyList()) +
                    CartItem(item, quantity, modifications)
        }
    }

    fun updateQuantity(item: MenuItem, mods: List<Modification>, qty: Int) {
        _cartItems.value = _cartItems.value!!.map {
            if (it.menuItem == item && it.modifications == mods) it.copy(quantity = qty)
            else it
        }
    }

    fun removeItem(item: MenuItem, mods: List<Modification>) {
        _cartItems.value = _cartItems.value!!.filterNot {
            it.menuItem == item && it.modifications == mods
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double =
        _cartItems.value!!.sumOf {
            (it.menuItem.price + it.modifications.sumOf { m -> m.extraCost }) * it.quantity
        }


}