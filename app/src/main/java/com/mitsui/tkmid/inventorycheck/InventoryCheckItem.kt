package com.mitsui.tkmid.inventorycheck

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_check_table")
data class InventoryCheckItem(
    @PrimaryKey(autoGenerate = true)
    var Id: Int = 0,

    @ColumnInfo(name = "checked_time_milli")
    val checkedTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "qr_code_value")
    var qrCodeValue: String = "",

    @ColumnInfo(name = "server_sync")
    var serverSync: String = "No"
)