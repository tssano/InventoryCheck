package com.mitsui.tkmid.inventorycheck

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface InventoryCheckDao {

    @Insert
    fun insert(checkItem: InventoryCheckItem)

    @Update
    fun update(checkItem: InventoryCheckItem)

    @Query("DELETE FROM inventory_check_table")
    fun clear()

    @Query("SELECT * FROM inventory_check_table ORDER BY Id DESC")
    fun getAllItems(): LiveData<List<InventoryCheckItem>>

    @Query("SELECT * FROM inventory_check_table ORDER BY Id DESC LIMIT 1")
    fun getLastItem(): InventoryCheckItem?
}