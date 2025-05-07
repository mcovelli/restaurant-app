package com.example.restaurantapp.model

data class CartItem(
    val menuItem: MenuItem,
    var quantity: Int,
    val modifications: List<Modification> = emptyList<Modification>()
)