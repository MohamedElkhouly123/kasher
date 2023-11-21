package com.example.ecommerceapp.utils.Dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity


class LogoutDialog {
    private lateinit var generalDataSendedModelPub: GeneralDataSendedModel

    //    private DialogAdapterCallback dialogAdapterCallback;
    private val checkOutTv: TextView? = null
    private val checkInTv: TextView? = null
    private val fromDateTv: TextView? = null
    private val toDateTv: TextView? = null
    private val hotelNameTv: TextView? = null
    private val typeTv: TextView? = null
    var editTexts: List<EditText> = ArrayList()

    //    private UserData userData;
    private val idPackage = 0
    private val idHotel = 0
    fun showDialog( generalDataSendedModel: GeneralDataSendedModel) {
        try {
            generalDataSendedModelPub=generalDataSendedModel
            val dialog = Dialog(generalDataSendedModel.activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_log_out)
            if (generalDataSendedModel.mustUpdate == 1) {
                dialog.setCanceledOnTouchOutside(false)
            }
            val hotelBookingCloseBtn =
                dialog.findViewById<View>(R.id.dialog_log_out_close_btn) as Button
            val bookNowBtn =
                dialog.findViewById<View>(R.id.dialog_log_out_btn) as Button
            hotelBookingCloseBtn.setOnClickListener { //                dialogAdapterCallback.onMethodCallback(getHomeDisscoverGetItemsListData.get(position));
                    dialog.cancel()
            }
            bookNowBtn.setOnClickListener {
                logOutFun(dialog)
            }
            dialog.show()
        }catch (e:Exception){}
    } //    private void onCall(Activity activity, Dialog dialog, HotelData hotelData, GetRoom getRoom) {
    private fun logOutFun(dialog: Dialog) {
//        String languageCode = LoadLanguage(getActivity(), "LANGUAGE");
//        languageToLoad = languageCode;
        SharedPreferencesManger.clean(generalDataSendedModelPub.activity!!)
        SharedPreferencesManger.SaveLanguage(generalDataSendedModelPub.activity!!, "LANGUAGE", BaseActivity.languageToLoad)
        generalDataSendedModelPub.navController!!.navigate(R.id.loginFragment)
        dialog.dismiss()
//        val i = Intent(generalDataSendedModelPub.activity, UserCycleActivity::class.java)
//        generalDataSendedModelPub.activity?.startActivity(i)
//        // close this activity
//        generalDataSendedModelPub.activity?.finish()
        onCall()
    }

    private fun onCall() {
//        Call<GetLogoutResponse> clientCall;
//        clientCall = ApiClient.getApiClient().logOutFunction(basicAuthorization);
//        viewModelUser.setLogout(getActivity(), clientCall, this::tryAgainCall);
    }
    //        String reserfedFrom = checkinDate.getDate_txt();
    //        String reserfedTo = checkoutDate.getDate_txt();
    ////        showToas(activity, String.valueOf(checkinDate.getDate_txt()));
    //        int userId = userData.getId();
    //        int hotelId = hotelData.getId();
    //        int roomId = getRoom.getId();
    //        Call<GetDiscoverHomeResponce> updateItemCal= null;
    //            updateItemCal = getApiClient().bookHotel(reserfedFrom, reserfedTo, userId, hotelId, roomId);
    //
    //
    //        sentUserRateAndBookHotelCallBack(activity,updateItemCal,"Success Hotel Booking");
    //        dialog.cancel();
    //
    //    }
    companion object {
        private const val adultNum = 0
    }
}