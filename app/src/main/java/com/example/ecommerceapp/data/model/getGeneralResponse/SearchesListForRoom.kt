package com.example.ecommerceapp.data.model.getGeneralResponse

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "clientSearches")
class SearchesListForRoom() {
    var searchTextToSave: String?=null
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    var itemId = 0
}