package com.example.restaurantapp.model

import androidx.annotation.DrawableRes

data class MenuItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val availableModifications: List<Modification> = emptyList(),
    @DrawableRes val imageResId: Int
)