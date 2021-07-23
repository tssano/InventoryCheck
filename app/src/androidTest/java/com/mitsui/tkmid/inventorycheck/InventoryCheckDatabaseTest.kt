package com.mitsui.tkmid.inventorycheck


import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mitsui.tkmid.inventorycheck.InventoryCheckItem
import com.mitsui.tkmid.inventorycheck.InventoryCheckDatabase
import com.mitsui.tkmid.inventorycheck.InventoryCheckDao
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class InventoryCheckDatabaseTest {

    private lateinit var inventoryCheckDao: InventoryCheckDao
    private lateinit var db: InventoryCheckDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, InventoryCheckDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        inventoryCheckDao = db.inventoryCheckDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val item = InventoryCheckItem()
        inventoryCheckDao.insert(item)
        val itemGet = inventoryCheckDao.getLastItem()
        assertEquals(itemGet?.serverSync, false)
    }
}