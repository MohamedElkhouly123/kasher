package com.example.ecommerceapp.view.main.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "clientAddresses")
class AddressesForRoom() :
    Serializable {
    var street: String? = null
    var state: String? = null
    var area: String? = null
    var country: String? = null
    var title: String?=null
    var selected: Boolean=false
    var lat: Double? =null
    var lang: Double? =null
    var address: String?=null
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    var itemId = 0

}
