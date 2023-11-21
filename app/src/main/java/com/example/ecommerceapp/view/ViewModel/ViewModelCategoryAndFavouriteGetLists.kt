package com.example.ecommerceapp.view.ViewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.netWork.InternetState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCategoryAndFavouriteGetLists : ViewModel() { //
    val makeGetGeneralResponse: MutableLiveData<GetGeneralResponse> = MutableLiveData<GetGeneralResponse>()

    fun getGeneralResponse(
        generalDataSendedModel: GeneralDataSendedModel,
        method: Call<GetGeneralResponse?>

    ) {
        if (InternetState.isConnected(generalDataSendedModel?.activity!!)) {
            method.enqueue(object : Callback<GetGeneralResponse?> {
                override fun onResponse(
                    call: Call<GetGeneralResponse?>,
                    response: Response<GetGeneralResponse?>
                ) {
                    GeneralViewModelMethods.checkError(
                        response.isSuccessful(), response.code(), response.errorBody()!!
                            .string(), generalDataSendedModel
                    )
                    if (response.body() != null) {
                        try {
                            if (response.body()!!.status === true) {
//                                ToastCreator.onCreateSuccessToast(activity, "Success");
                                generalDataSendedModel.load_more?.setVisibility(View.GONE)
                                generalDataSendedModel.fragment_sr_refresh?.setRefreshing(false)
                                makeGetGeneralResponse.postValue(response.body())
                            } else {
                                ToastCreator.onCreateErrorToast(
                                    generalDataSendedModel?.activity!!,
                                    response.body()?.message
                                )
                            }
                        } catch (e: Exception) {
                        }
                    } else {
                    }
                }

                override fun onFailure(call: Call<GetGeneralResponse?>, t: Throwable) {
                    GeneralViewModelMethods.onFailFun(generalDataSendedModel, t)
                    makeGetGeneralResponse.postValue(null)
                }
            })
        } else {
            GeneralViewModelMethods.noInternetFun(generalDataSendedModel)
        }
    }

}