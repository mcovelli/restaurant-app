package com.example.restaurantapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderHistoryDao {
    @Insert
    suspend fun insertOrder(order: OrderHistoryEntity)

    @Query("SELECT * FROM order_history ORDER BY id DESC")
    suspend fun getAllOrders(): List<OrderHistoryEntity>
    @Query("DELETE FROM order_history")
    suspend fun clearAllOrders()



}