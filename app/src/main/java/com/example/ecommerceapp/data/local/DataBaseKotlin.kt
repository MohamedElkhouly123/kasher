package com.example.ecommerceapp.view.main.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecommerceapp.data.model.getGeneralResponse.SearchesListForRoom
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.example.ecommerceapp.view.main.data.models.AllProductForRom


@Database(
    entities = [AllProductForRom::class, AddressesForRoom::class, SearchesListForRoom::class],
    version = 9,
    exportSchema = false
) //@Database(entities = {Client.class, ClientData.class}, version = 1, exportSchema = false)  // more than ClientMakeNewOrderItem
//@TypeConverters({DataTypeConverter.class})
abstract class DataBaseKotlin : RoomDatabase() {
    // endregion
    // region DAOs
    abstract fun addNewOrderItemDao(): NewsItemsForRoomDaoKotlin?

    companion object {
        private const val DB_NAME = "databaseEccommerceApp.db"

        @Volatile
        private var instance: DataBaseKotlin? = null

        // region singleton implementation
        @Synchronized
        fun getInstance(context: Context): DataBaseKotlin? {
            if (instance == null) {
                instance = create(context)
            }
            return instance
        }

        private fun create(context: Context): DataBaseKotlin {
            val builder = Room.databaseBuilder(
                context,
                DataBaseKotlin::class.java, DB_NAME
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
            return builder.build()
        }
    }
}
