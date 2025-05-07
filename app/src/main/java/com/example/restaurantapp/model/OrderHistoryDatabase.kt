package com.example.restaurantapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OrderHistoryEntity::class], version = 1, exportSchema = false)
abstract class OrderHistoryDatabase : RoomDatabase() {
    abstract fun orderHistoryDao(): OrderHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: OrderHistoryDatabase? = null

        fun getDatabase(context: Context): OrderHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderHistoryDatabase::class.java,
                    "order_history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}