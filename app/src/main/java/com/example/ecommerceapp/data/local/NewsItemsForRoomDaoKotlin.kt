package com.example.ecommerceapp.view.main.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ecommerceapp.data.model.getGeneralResponse.SearchesListForRoom
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.example.ecommerceapp.view.main.data.models.AllProductForRom

@Dao
interface NewsItemsForRoomDaoKotlin {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg productItem: AllProductForRom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg adresssItem: AddressesForRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg productItem: SearchesListForRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllOrders(productItem: List<AllProductForRom>)

    @Insert
    fun add(vararg productItem: AllProductForRom)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg productItem: AllProductForRom)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg adresssItem: AddressesForRoom)

    @Query("UPDATE clientEcommerceNewOrder SET quantity=:quan WHERE itemId = :id")
    fun update(quan: String?, id: Int)

    @Query("UPDATE clientAddresses SET selected=:selectedValue WHERE itemId = :id")
    fun updateSelected(selectedValue: Boolean?, id: Int)

    //    @Query("UPDATE clientNewOrderSahl SET quantity=:quan WHERE itemId = :id")
    //    void update(String quan, int id);
    @Delete
    fun delete(vararg productItem: AllProductForRom)

    @Query("DELETE FROM clientEcommerceNewOrder WHERE itemId = :id")
    fun deleteById(id: Int): Int

    @Query("DELETE FROM clientAddresses WHERE itemId = :id")
    fun deleteAddressesById(id: Int): Int

    @Query("DELETE FROM clientSearches WHERE itemId = :id")
    fun deleteSearchesById(id: Int): Int

    @Update
    fun updateArticleDate(productItem: AllProductForRom)

    @get:Query("SELECT * FROM clientEcommerceNewOrder ")
    val allItems: List<AllProductForRom?>

    @get:Query("SELECT * FROM clientAddresses ")
    val allAddressesItems: List<AddressesForRoom?>?


    @get:Query("SELECT * FROM clientSearches ")
    val allSearchesItems: List<SearchesListForRoom?>?

    @Query("SELECT * FROM clientEcommerceNewOrder ORDER BY itemId DESC LIMIT 1")
    fun loadLast(): AllProductForRom

    @Query("SELECT * FROM clientEcommerceNewOrder WHERE itemId=:id ")
    fun loadSingle(id: Int): AllProductForRom

    @Query("SELECT * FROM clientEcommerceNewOrder WHERE productName=:name ")
    fun loadSingleByName(name: String): AllProductForRom

    @Query("SELECT * FROM clientAddresses WHERE selected=:selectedValue ")
    fun loadSingleAddressSelected(selectedValue: Boolean): AddressesForRoom
    @Query("SELECT * FROM clientAddresses WHERE itemId=:selectedValue ")
    fun loadSingleAddressById(selectedValue: Int): AddressesForRoom
    //    LiveData<List<ClientMakeNewOrderItemForRoom>> getAllItems();
    //
    //
    //    @Query("SELECT * FROM clientNewOrder WHERE itemId = :id")
    //    LiveData<AllProductForRom> getSubjectById(int id);
    //    @Query("select * from clientNewOrder where active = 1;")
    //    ClientData checkIfUserExist();
    @Query("DELETE  FROM clientEcommerceNewOrder")
    fun deletAll()

    @Query("DELETE  FROM clientAddresses")
    fun deletAllAddress()

    @Query("DELETE  FROM clientSearches")
    fun deletAllSearches()
}