package com.mitsui.tkmid.inventorycheck


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InventoryCheckItem::class], version = 2, exportSchema = false)
abstract class InventoryCheckDatabase : RoomDatabase() {

    abstract val inventoryCheckDao: InventoryCheckDao

    companion object {

        @Volatile
        private var INSTANCE: InventoryCheckDatabase? = null

        fun getInstance(context: Context): InventoryCheckDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InventoryCheckDatabase::class.java,
                        "inventory_check_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}
