package com.example.ecommerceapp.utils

import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.utils.HelperMethod.dismissProgressDialog
import com.example.ecommerceapp.utils.netWork.InternetState.isConnected
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.checkError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GeneralRequest {

    fun makeGeneralAction(generalDataSendedModel: GeneralDataSendedModel,
                          method: Call<GetGeneralResponse?>) {
        // TODO: Implement this method to send token to your app server.
        if (isConnected(generalDataSendedModel?.activity!!)) {
            GeneralViewModelMethods.progressStartFun(generalDataSendedModel)
            method.enqueue(object : Callback<GetGeneralResponse?> {
                override fun onResponse(
                    call: Call<GetGeneralResponse?>,
                    response: Response<GetGeneralResponse?>
                ) {
                    try {
                        checkError(
                            response.isSuccessful(),
                            response.code(),
                            response.errorBody()!!.string(),
                            generalDataSendedModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (response.body() != null) {
                        try {
                            dismissProgressDialog()
                            ToastCreator.onCreateSuccessToast(
                                generalDataSendedModel?.activity!!,
                                response.body()?.message
                            )
                        } catch (e: Exception) {
                        }
                    } else {
                    }
                }
                override fun onFailure(call: Call<GetGeneralResponse?>, t: Throwable) {
                    GeneralViewModelMethods.onFailFun(generalDataSendedModel, t)
                }
            })
        } else {
            GeneralViewModelMethods.noInternetFun(generalDataSendedModel)
        }
    }

    fun makeGeneralActionWithoutProgress(generalDataSendedModel: GeneralDataSendedModel,
                          method: Call<GetGeneralResponse?>) {
        // TODO: Implement this method to send token to your app server.
        if (isConnected(generalDataSendedModel?.activity!!)) {
            method.enqueue(object : Callback<GetGeneralResponse?> {
                override fun onResponse(
                    call: Call<GetGeneralResponse?>,
                    response: Response<GetGeneralResponse?>
                ) {
                    try {
                        checkError(
                            response.isSuccessful(),
                            response.code(),
                            response.errorBody()!!.string(),
                            generalDataSendedModel
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (response.body() != null) {
                        try {
                            dismissProgressDialog()
                            ToastCreator.onCreateSuccessToast(
                                generalDataSendedModel?.activity!!,
                                response.body()?.message
                            )
                        } catch (e: Exception) {
                        }
                    } else {
                    }
                }
                override fun onFailure(call: Call<GetGeneralResponse?>, t: Throwable) {
                    GeneralViewModelMethods.onFailFun(generalDataSendedModel, t)
                }
            })
        } else {
            GeneralViewModelMethods.noInternetFun(generalDataSendedModel)
        }
    }


}