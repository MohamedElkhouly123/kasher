package com.example.ecommerceapp.data.model

import android.app.Activity
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.example.ecommerceapp.data.api.ApiClient.apiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.utils.GeneralRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import retrofit2.Call

class ClientFireBaseToken(var generalDataSendedModel: GeneralDataSendedModel) :
    FirebaseMessagingService() {
    private val myGenderType: String? = null

    //    @Override
    //    public void onNewToken(String token) {
    //        Log.d(TAG, "Refreshed token: " + token);
    //
    //        // If you want to send messages to this application instance or
    //        // manage this apps subscriptions on the server side, send the
    //        // FCM registration token to your app server.
    //    }
    var token: String? = null

    init {
        val refreshedToken = FirebaseMessaging.getInstance().token
        // Get token
        // [START retrieve_current_token]
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(Constraints.TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val firebasetoken = task.result
                val tokenCall: Call<GetGeneralResponse?>
                //                        Log.i(ContentValues.TAG,"firebasetoken"+String.valueOf(firebasetoken));
                tokenCall = apiClient?.saveFireBaseToken(refreshedToken.toString())!!
                GeneralRequest.makeGeneralActionWithoutProgress(
                    generalDataSendedModel,
                    tokenCall
                )
            //                        Toast.makeText(activity, firebasetoken+"", Toast.LENGTH_SHORT).show();
            })
        //        setToken( token);
    }

}