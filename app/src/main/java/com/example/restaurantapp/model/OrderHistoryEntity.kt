package com.example.restaurantapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_history")
data class OrderHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNumber: String,
    val itemsSummary: String,
    val total: Double,
    val readyTime: String,
    val orderDate: String
)