package com.example.ecommerceapp.utils.Dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.HelperMethod.openGoogleCroomLink

class UpdateNowDialog {
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
            val dialog = Dialog(generalDataSendedModel.activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.dialog_update_now)
            if (generalDataSendedModel.mustUpdate == 1) {
                dialog.setCanceledOnTouchOutside(false)
            }
            val hotelBookingCloseBtn =
                dialog.findViewById<View>(R.id.dialog_update_now_close_btn) as Button
            val bookNowBtn =
                dialog.findViewById<View>(R.id.dialog_update_now_update_now_btn) as Button
            hotelBookingCloseBtn.setOnClickListener { //                dialogAdapterCallback.onMethodCallback(getHomeDisscoverGetItemsListData.get(position));
                if (generalDataSendedModel.mustUpdate == 0) {
                    dialog.cancel()
                }
            }
            bookNowBtn.setOnClickListener { //                    ToastCreator.onCreateErrorToast(activity, activity.getString(R.string.select_date));
                val url2 =
                    "https://play.google.com/store/apps/details?id=com.shopping.ecommerceapp"
                generalDataSendedModel?.googleUrl=url2
                openGoogleCroomLink(generalDataSendedModel!!)
            }
            dialog.show()
        }catch (e:Exception){}
    } //    private void onCall(Activity activity, Dialog dialog, HotelData hotelData, GetRoom getRoom) {

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