package com.example.ecommerceapp.data.model.getGeneralResponse

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ecommerceapp.adapter.AddressesAdapter
import com.example.ecommerceapp.data.model.BasicCountryItem
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.ProductAdaptersActions
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.activity.UserCycleActivity
import com.example.ecommerceapp.view.fragment.subPages.AddressFragment
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom

class GeneralDataSendedModel {


    lateinit var countryItem: BasicCountryItem
    var googleUrl: String?=null
    val card: CardView? = null
    var no_products: LinearLayout?=null
    var addressAdapter: AddressesAdapter?=null
    var gpsOpened2: Boolean?=false
    var from: String?=null
    var gpsOpened: Boolean?=false
    var mapWaitTextView: TextView?=null
    var longituate: Double?=0.0
    var latituate: Double?=0.0
    var gpsCanceled: Boolean?=false
    var generalBundelDataToSend: GeneralBundelDataToSend?=null
    var productAdaptersActions: ProductAdaptersActions?=null
    var mustUpdate: Int? = 0
    var addressSelectedItem: AddressesForRoom? = AddressesForRoom()
    var type2: String?=null
    var context: Context?=null
    var navController: NavController?=null
    var makeChangeInFragment: MakeChangeInFragment?=null
    var backIfCancelLogin: Boolean=false
    var startLogin: Boolean?=false
    var maxPage: Int?=null
    var load_more: RelativeLayout?=null
    var fragment_sr_refresh: SwipeRefreshLayout?=null
    var userCycleActivity: UserCycleActivity?=null
    var type: String?=null
    var homeCycleActivity: HomeCycleActivity?=null
    var tryAgainCall: TryAgainOncall? =null
    var password: String? = null
    var appLanguage: String? = null
    var remember: Boolean? = null
    var activity : Activity? =null
}