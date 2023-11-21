package com.example.ecommerceapp.view.main.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "clientEcommerceNewOrder")
class AllProductForRom     //        this.itemId = itemId;
//        this.productId = productId;
//        this.created = created;
    (
    @field:Expose
    @field:SerializedName("product_name")
    var productName: String,
    @field:Expose
    @field:SerializedName("product_price")
    var productPrice: String,
    @field:Expose
    @field:SerializedName("product_cat")
    var productCat: String,
    @field:Expose
    @field:SerializedName("vendor_id_fk")
    var vendorIdFk: String,
    @field:Expose
    @field:SerializedName("product_specification")
    var productSpecification: String,
    @field:Expose
    @field:SerializedName("product_desc")
    var productDesc: String,
    @field:Expose
    @field:SerializedName("image")
    var image: String,
    @field:Expose
    @field:SerializedName("in_stock")
    var inStock: String,
    @field:Expose
    @field:SerializedName("hotdeals")
    var hotdeals: String,
    @field:Expose
    @field:SerializedName("main_category_name")
    var mainCategoryName: String,
    @field:Expose
    @field:SerializedName("vendor_name")
    var vendorName: String,
    @field:Expose
    @field:SerializedName("product_qty")
    var quantity: String
) :
    Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId")
    var itemId = 0

    @SerializedName("product_id")
    @Expose
    var productId: Int? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("updated")
    @Expose
    var updated: String? = null

}
