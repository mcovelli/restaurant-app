package com.example.restaurantapp.model

data class OrderHistoryItem(
    val orderNumber: String,
    val itemsSummary: String,
    val total: Double,
    val readyTime: String,
    val orderDate: String
)