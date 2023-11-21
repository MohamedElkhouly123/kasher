package com.example.ecommerceapp.view.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger.REMEMBER_ME
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveData
import com.example.ecommerceapp.data.local.SharedPreferencesManger.USER_DATA
import com.example.ecommerceapp.data.local.SharedPreferencesManger.USER_PASSWORD
import com.example.ecommerceapp.data.local.SharedPreferencesManger.USER_TOKEN
import com.example.ecommerceapp.data.local.SharedPreferencesManger.clean
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.utils.HelperMethod.dismissProgressDialog
import com.example.ecommerceapp.utils.HelperMethod.showCookieMsg
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.ToastCreator.onCreateErrorToast
import com.example.ecommerceapp.utils.netWork.InternetState.isConnected
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.checkError
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.noInternetFun
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.onFailFun
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.progressStartFun
import com.example.ecommerceapp.view.ViewModel.GeneralViewModelMethods.showAgreeDialog
import org.aviran.cookiebar2.CookieBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelUserAndPostRequests : ViewModel() { //
    val loginAndRegister: MutableLiveData<GetRegisterResponse> =
        MutableLiveData<GetRegisterResponse>()
    val getGeneralAndLogout: MutableLiveData<GetGeneralResponse> = MutableLiveData<GetGeneralResponse>()

    fun setLoginAndRegister(
        generalDataSendedModel: GeneralDataSendedModel,
        method: Call<GetRegisterResponse?>
    ) {
        if (isConnected(generalDataSendedModel.activity!!)) {
            progressStartFun(generalDataSendedModel)
            method.enqueue(object : Callback<GetRegisterResponse?> {
                override fun onResponse(
                    call: Call<GetRegisterResponse?>,
                    response: Response<GetRegisterResponse?>
                ) {
//                  showToast(activity, String.valueOf("true"));
                    if (response != null) {
                        try {
                            checkError(
                                response.isSuccessful(), response.code(), response.errorBody()!!
                                    .string(), generalDataSendedModel
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            if (response.body()?.status === true) {
                                val userId: String =
                                    response.body()?.getRegisterUserDataResponse?.id!!
                                //                                ClientFireBaseToken clientFireBaseToken=new ClientFireBaseToken(activity,userId,genderType);
                                clean(generalDataSendedModel.activity!!)
                                SaveData(
                                    generalDataSendedModel.activity!!,
                                    USER_PASSWORD,
                                    generalDataSendedModel.password!!
                                )
//                                lang?.let { SaveData(activity, LANG, it) } ?: SaveData(
//                                    activity,
//                                    LANG,
//                                    "en"
//                                )
                                SaveData(
                                    generalDataSendedModel.activity!!,
                                    USER_DATA,
                                    response.body()?.getRegisterUserDataResponse
                                )
                                SaveData(
                                    generalDataSendedModel.activity!!,
                                    REMEMBER_ME,
                                    generalDataSendedModel.remember!!
                                )
                                SaveData(
                                    generalDataSendedModel.activity!!,
                                    USER_TOKEN,
                                    response.body()?.getRegisterUserDataResponse?.token
                                )
                                val basicAuthorization =
                                    "Bearer " + response.body()?.getRegisterUserDataResponse
                                        ?.token
//                                ClientFireBaseToken(activity, basicAuthorization)
                                dismissProgressDialog()
                                loginAndRegister.postValue(response.body())
//                                val cookie = response.headers()["Set-Cookie"]
//                                BASE_cook = cookie!!.split(";").toTypedArray()[0]
//                                SaveData(activity, USER_Cookie, BASE_cook)
//                                Log.i(
//                                    ContentValues.TAG,
//                                    cookie.split(";").toTypedArray()[0]
//                                )
                            } else {
                                dismissProgressDialog()
                                if (response.body()?.same_device === 1) {
                                    showAgreeDialog(
                                        generalDataSendedModel.activity!!,
                                        response.body()
                                    )
                                } else {
                                    showCookieMsg(
                                        generalDataSendedModel.activity!!.getString(R.string.warning),
                                        response.body()?.message,
                                        generalDataSendedModel.activity!!,
                                        R.color.red2,
                                        CookieBar.TOP
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            dismissProgressDialog()
                        }
                    } else {
                        dismissProgressDialog()
                        onCreateErrorToast(
                            generalDataSendedModel.activity!!,
                            response.body()?.message
                        )
                    }
                }
                override fun onFailure(call: Call<GetRegisterResponse?>, t: Throwable) {
                   onFailFun(generalDataSendedModel,t)
                    loginAndRegister.postValue(null)
                }
            })
        } else {
            noInternetFun(generalDataSendedModel)
        }
    }


    fun setGeneralAndLogout(
        generalDataSendedModel: GeneralDataSendedModel,
        method: Call<GetGeneralResponse?>,
    ) {
        if (isConnected(generalDataSendedModel?.activity!!)) {
            progressStartFun(generalDataSendedModel)
            method.enqueue(object : Callback<GetGeneralResponse?> {
                override fun onResponse(
                    call: Call<GetGeneralResponse?>,
                    response: Response<GetGeneralResponse?>
                ) {
                    if (response != null) {
                        try {
                            try {
                                checkError(
                                    response.isSuccessful(), response.code(), response.errorBody()!!
                                        .string(), generalDataSendedModel
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (response.body()?.status === true) {
                                dismissProgressDialog()
                                getGeneralAndLogout.postValue(response.body())
                                ToastCreator.onCreateSuccessToast(generalDataSendedModel?.activity!!, "Success Log Out")
                            } else {
                                dismissProgressDialog()
                                onCreateErrorToast(generalDataSendedModel?.activity!!, response.body()?.message)
                            }
                        } catch (e: Exception) {
                            dismissProgressDialog()
                        }
                    } else {
                        dismissProgressDialog()
//                        onCreateErrorToast(generalDataSendedModel?.activity!!, response.body()?.message)
                    }
                }

                override fun onFailure(call: Call<GetGeneralResponse?>, t: Throwable) {
                    onFailFun(generalDataSendedModel,t)
                    getGeneralAndLogout.postValue(null)
                }
            })
        } else {
            noInternetFun(generalDataSendedModel)
        }
    }


    fun setGeneralAndLogoutWithoutProgress(
        generalDataSendedModel: GeneralDataSendedModel,
        method: Call<GetGeneralResponse?>,
    ) {
        if (isConnected(generalDataSendedModel?.activity!!)) {
            method.enqueue(object : Callback<GetGeneralResponse?> {
                override fun onResponse(
                    call: Call<GetGeneralResponse?>,
                    response: Response<GetGeneralResponse?>
                ) {
                    if (response != null) {
                        try {
                            try {
                                checkError(
                                    response.isSuccessful(), response.code(), response.errorBody()!!
                                        .string(), generalDataSendedModel
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (response.body()?.status === true) {
                                getGeneralAndLogout.postValue(response.body())
                                ToastCreator.onCreateSuccessToast(generalDataSendedModel?.activity!!, "Success Log Out")
                            } else {
                                onCreateErrorToast(generalDataSendedModel?.activity!!, response.body()?.message)
                            }
                        } catch (e: Exception) {
                        }
                    } else {
                        onCreateErrorToast(generalDataSendedModel?.activity!!, response.body()?.message)
                    }
                }
                override fun onFailure(call: Call<GetGeneralResponse?>, t: Throwable) {
                    onFailFun(generalDataSendedModel,t)
                }
            })
        } else {
           noInternetFun(generalDataSendedModel)
        }
    }



}