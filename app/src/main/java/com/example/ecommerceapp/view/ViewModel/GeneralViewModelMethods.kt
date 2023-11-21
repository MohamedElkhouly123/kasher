package com.example.ecommerceapp.view.ViewModel

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.utils.Dialogs.UpdateNowDialog
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.ToastCreator
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import org.aviran.cookiebar2.CookieBar
import org.json.JSONObject
import retrofit2.Call

object GeneralViewModelMethods {
    private lateinit var appUpdateManager: AppUpdateManager

    fun noInternetFun(generalDataSendedModel: GeneralDataSendedModel) {
        try {
//            if(generalDataSendedModel.userCycleActivity!=null) {
                generalDataSendedModel.userCycleActivity!!.checkInternetFun(
                    generalDataSendedModel
                )
//            }
            sendDataToHomeActivty(generalDataSendedModel)
            HelperMethod.dismissProgressDialog()
            generalDataSendedModel.load_more?.setVisibility(View.GONE)
            generalDataSendedModel.fragment_sr_refresh?.setRefreshing(false)
        } catch (e: Exception) {
            HelperMethod.showToast(generalDataSendedModel.activity, ""+e)
        }
        HelperMethod.showCookieMsg(
            generalDataSendedModel?.activity!!.getString(R.string.warning),
            generalDataSendedModel?.activity!!.getString(R.string.error_inter_net),
            generalDataSendedModel?.activity!!,
            R.color.red2,
            CookieBar.TOP
        )
    }

    private fun sendDataToHomeActivty(generalDataSendedModel: GeneralDataSendedModel) {
        if (generalDataSendedModel?.tryAgainCall!! != null) {
            if(generalDataSendedModel.homeCycleActivity!=null) {
                generalDataSendedModel.homeCycleActivity?.checkUserAuthAndInternetFun(
                    generalDataSendedModel
                )
            }
        }
    }

    fun checkUpdateApp(generalDataSendedModel: GeneralDataSendedModel) {
        appUpdateManager = AppUpdateManagerFactory.create(generalDataSendedModel.activity!!)

// Returns an intent object that you use to check for an update.
        val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.getAppUpdateInfo()

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE // This example applies an immediate update. To apply a flexible update
            // instead, pass in AppUpdateType.FLEXIBLE
            ) {

//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//                            appUpdateInfo,
//                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//                            IMMEDIATE,
//                            // The current activity making the update request.
//                            this,
//                            // Include a request code to later monitor this update request.
//                            1);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//                val dialog1 = UpdateNowDialog(activity?, navController)
//                dialog1.show(this.getSupportFragmentManager(), "example")
                val dialog2 = UpdateNowDialog()
                dialog2.showDialog(generalDataSendedModel)
                // Request the update.
            } else {
            }
        }
    }

    fun progressStartFun(generalDataSendedModel: GeneralDataSendedModel) {
        if (HelperMethod.progressDialog == null) {
            HelperMethod.showProgressDialog(generalDataSendedModel?.activity!!, generalDataSendedModel?.activity!!.getString(
                R.string.wait))
        } else {
            if (!HelperMethod.progressDialog!!.isShowing()) {
                HelperMethod.showProgressDialog(generalDataSendedModel?.activity!!, generalDataSendedModel?.activity!!.getString(
                    R.string.wait))
            }
        }
    }


    fun onFailFun(generalDataSendedModel: GeneralDataSendedModel, t: Throwable) {
        try {
            HelperMethod.dismissProgressDialog()
            //                    showToast(activity, String.valueOf(t.getLocalizedMessage()));
            Log.i(ContentValues.TAG, t.message.toString())
            Log.i(ContentValues.TAG, t.cause.toString())
            ToastCreator.onCreateErrorToast(
                generalDataSendedModel.activity!!,
                generalDataSendedModel.activity!!.getString(R.string.error)
            )
            generalDataSendedModel.load_more?.setVisibility(View.GONE)
            generalDataSendedModel.fragment_sr_refresh?.setRefreshing(false)
        }catch (e:Exception){}
    }

    fun showAgreeDialog(activity: Activity, body: GetRegisterResponse?) {
        try {
            val alertDialog: AlertDialog
            alertDialog = AlertDialog.Builder(activity).create()
            //            alertDialog.setTitle("Delete");
            alertDialog.setMessage(body?.message)
            alertDialog.setCancelable(true)
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.continuee)
            ) { dialog, which ->
                val disableAccountOnCall: Call<GetGeneralResponse>
//                disableAccountOnCall =
//                    apiClient.disableUserAccountWhenAgree(userEmail)
//                agreeAcountDisable(disableAccountOnCall, activity)
            }
            alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel)
            ) { dialog, which -> alertDialog.dismiss() }
            alertDialog.show()
        } catch (e: Exception) {
        }
    }


    fun checkError(
        successfulResponse: Boolean,
        responseCode: Int,
        jObjError: String?,
        generalDataSendedModel: GeneralDataSendedModel
    ) {
        try {
//            ToastCreator.onCreateSuccessToast(activity, ""+responseCode);
            checkCodeError(
                successfulResponse,
                responseCode,
                generalDataSendedModel,generalDataSendedModel.activity!!
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!successfulResponse) {
            //response is not successful so response.body is null but response.errorBody is not null
            val message = "Server Error!"
            try {
                HelperMethod.dismissProgressDialog()
                val jObjErrors = JSONObject(jObjError)
                if ("false".equals(jObjErrors.getString("status"), ignoreCase = true)) {
                    HelperMethod.showCookieMsg(
                        generalDataSendedModel.activity?.getString(R.string.warning),
                        jObjErrors.getString("message"),
                        generalDataSendedModel.activity,
                        R.color.red2,
                        CookieBar.TOP
                    )
                    //                    onCreateErrorToast(activity, jObjError.getString("message"));
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkCodeError(
        successfulResponse: Boolean,
        responseCode: Int,
        generalDataSendedModel: GeneralDataSendedModel,
        activity: Activity
    ) {
        if (responseCode == 401) {
//            HelperMethod.showConfirmationDialog(generalDataSendedModel)
            generalDataSendedModel.startLogin=true
            sendDataToHomeActivty(generalDataSendedModel)
            return
        }
        if (responseCode == 429) {
            HelperMethod.showCookieMsg(
                generalDataSendedModel.activity!!.getString(R.string.warning),
                generalDataSendedModel.activity!!.getString(R.string.to_many_requests),
                generalDataSendedModel.activity!!,
                R.color.red2,
                CookieBar.TOP
            )
            return
        }
    }

}