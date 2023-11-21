package com.example.ecommerceapp.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SELECTED
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveData
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.GPSTracker
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.DatePickerFragment.Companion.canToggleGPS
import com.example.ecommerceapp.utils.HelperMethod.DatePickerFragment.Companion.turnGPSOn
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class AddressesAdapter(
    var adressesForRoomList: MutableList<AddressesForRoom>,
    var generalDataSendedModel: GeneralDataSendedModel?
) :
    RecyclerView.Adapter<AddressesAdapter.OrdersViewHolder>() {
    private lateinit var currentHolder: OrdersViewHolder
    private var bundle: Bundle? =null
    private var payload: String? = null
    private var selectedItem: Int = -1
    private var dataBase: DataBaseKotlin? = null
    private val activity: BaseActivity?
    private lateinit var locationManager: LocationManager
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_adress_item, null, false)
        return OrdersViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setData(holder, position)
        setAction(holder, position)
        if ("home".equals(generalDataSendedModel?.type)) {
            if (payload != null && payload!!.contains("BACKGROUND")) {
                updateBackgroud(holder, position)
            }
        }
    }

    private fun updateBackgroud(holder: OrdersViewHolder, position: Int) {
        try{
            var addressItem = adressesForRoomList[position]!!
                if (getItemViewType(position) == selectedItem) {
                    Executors.newSingleThreadExecutor()
                        .execute {
                            addressItem.selected = true
                            if (getItemViewType(position) == 0) {
                                SaveData(activity!!, SELECTED, "yes")
                            } else {
                                dataBase!!.addNewOrderItemDao()!!.update(addressItem)
                            }
                        }
                    if (getItemViewType(position) > 0) {
                        holder.card_adress_item_card_background_ly!!.setBackgroundColor(
                            activity!!.getResources().getColor(R.color.app_second_color_sky)
                        )
                        holder.card_adress_item_details_tv?.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                    }
                    adressesForRoomList[position].selected = true
                } else {
                    Executors.newSingleThreadExecutor()
                        .execute {

                            addressItem.selected = false
                            if (getItemViewType(position) == 0) {
                                SaveData(activity!!, SELECTED, "no")
                            } else {
                                dataBase!!.addNewOrderItemDao()!!.update(addressItem)
                            }
                        }
                    holder.card_adress_item_card_background_ly!!.setBackgroundColor(
                        activity!!.getResources().getColor(R.color.white)
                    )
                    holder.card_adress_item_details_tv?.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.gray_707070
                        )
                    )
                    adressesForRoomList[position].selected = false
            }
        }catch (e:Exception){}
    }

    private fun setAction(holder: OrdersViewHolder, position: Int) {
        try {

            var addressItem = adressesForRoomList[position]
            holder.card_adress_item_card_background_ly!!.setOnClickListener {
                if ("home".equals(generalDataSendedModel?.type)) {


                    if(getItemViewType(position)==0&&!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        GPSTracker.requestDeviceLocationSettings(generalDataSendedModel)
                        currentHolder=holder
                        generalDataSendedModel?.gpsOpened2=true
                        generalDataSendedModel?.makeChangeInFragment?.doChanges(generalDataSendedModel!!)

//                             HelperMethod.showDialoge(activity!!, "",
//                                activity?.getString(R.string.for_better_experience_turnon_gps),
//                                 activity?.getString(R.string.ok),
//                                 activity?.getString(R.string.Skip),
//                                { dialog: DialogInterface?, ok: Int -> openCheckGps() }
//                            ) { dialog: DialogInterface, no: Int ->
//                                dialog.dismiss()
//                            }
                    }else {
                        selectedItem = position
                        payload = "BACKGROUND"
                        notifyItemRangeChanged(0, itemCount, "BACKGROUND")
//                    object : CountDownTimer(500, 500) {
//                        override fun onTick(millisUntilFinished: Long) {
                        activity?.onBackPressed()
//                        }
//                        override fun onFinish() {
//                        }
//                    }.start()
                    }
                    }
            }
            holder.card_adress_item_edit_btn?.setOnClickListener {
                bundle?.putString("from", "edit_location")
                bundle?.putSerializable("addressItemData", addressItem);
                navController.navigate(R.id.mapFragment, bundle)

            }
            holder.cardAdressItemDeleteBtn?.setOnClickListener {
                Executors.newSingleThreadExecutor()
                    .execute {
                        dataBase!!.addNewOrderItemDao()!!.deleteAddressesById(addressItem.itemId)
                        //            showToast(getActivity(), notificationPosition + "");
                        runBlocking (Dispatchers.Main) {
                            holder.view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rv_animation_left_to_right2));
                            Handler().postDelayed({
                                if (adressesForRoomList.size>0&&adressesForRoomList.size>position) {
                                    adressesForRoomList?.removeAt(position)
                                    notifyItemRemoved(position)
                                    notifyItemRangeChanged(
                                        0,
                                        getItemCount()
                                    )
                                }
                                if (adressesForRoomList.size == 0) {
                                    generalDataSendedModel?.makeChangeInFragment?.doChanges(generalDataSendedModel!!)
                                }
                            }, 600)
                        }
                    }
            }
        }catch (e:Exception){}

    }

    fun startLoading() {
        try {
            currentHolder.card_adress_item_details_tv?.text =
                activity!!.getString(R.string.current_location_is_loading)
            currentHolder.card_adress_item_details_progress_loading?.visibility = View.VISIBLE
        }catch (e:Exception){}
    }

    fun stopLoading() {
        try {
            currentHolder.card_adress_item_details_tv?.text =
                activity!!.getString(R.string.enable_device_location)
            currentHolder.card_adress_item_details_progress_loading?.visibility = View.GONE
        }catch (e:Exception){}
    }
    private fun openCheckGps() {
        if (canToggleGPS(activity!!)){
            turnGPSOn(activity!!)
        }else {
            activity?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun setData(holder: OrdersViewHolder, position: Int) {
        var addressItem = adressesForRoomList[position]
        dataBase = DataBaseKotlin.getInstance(activity!!)
        bundle=Bundle()
        holder.card_adress_item_title_tv?.text=addressItem.title
        if (getItemViewType(position) > 0||!"home".equals(generalDataSendedModel?.type)) {
            holder.card_adress_item_details_tv?.text = addressItem.address
        }
        if (!"home".equals(generalDataSendedModel?.type)){
            holder.card_adress_item_edit_ly?.visibility=View.VISIBLE
        }else{
            if (getItemViewType(position) == 0&&"add".equals(generalDataSendedModel?.type2)) {
                holder.card_adress_item_title_tv?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_round_my_location_24px_map,
                    0,
                    0,
                    0
                )
                if(addressItem.address!=null&&addressItem.address?.trim()!!.length>0&&locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    holder.card_adress_item_details_tv?.text =addressItem.address
//                        activity.getString(R.string.enable_device_location)
                }
            }
            if (getItemViewType(position) > 0||!"home".equals(generalDataSendedModel?.type)) {

                if(addressItem.selected){
                holder.card_adress_item_card_background_ly!!.setBackgroundColor(
                    activity!!.getResources().getColor(R.color.app_second_color_sky)
                )
                holder.card_adress_item_details_tv?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )

            }else{
                holder.card_adress_item_card_background_ly!!.setBackgroundColor(
                    activity!!.getResources().getColor(R.color.white)
                )
                holder.card_adress_item_details_tv?.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.gray_707070
                    )
                )
            }
        }
        }
    }
    override fun getItemCount(): Int {
        return adressesForRoomList.size
//        return 12
    }

    inner class OrdersViewHolder(val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var cardAdressItemDeleteBtn: TextView? = view.findViewById(R.id.card_adress_item_delete_btn)
        var card_adress_item_edit_btn: TextView? = view.findViewById(R.id.card_adress_item_edit_btn)
        var card_adress_item_title_tv: TextView? = view.findViewById(R.id.card_adress_item_title_tv)
        var card_adress_item_details_tv: TextView? = view.findViewById(R.id.card_adress_item_details_tv)
        var card_adress_item_details_progress_loading: ProgressBar? = view.findViewById(R.id.card_adress_item_details_progress_loading)

        var card_adress_item_edit_ly: LinearLayout? = view.findViewById(R.id.card_adress_item_edit_ly)
        var card_adress_item_card_background_ly: LinearLayout? = view.findViewById(R.id.card_adress_item_card_background_ly)

//        var cardParent: CardView? = view.card_parent

    }

    init {
        this.activity = generalDataSendedModel?.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}