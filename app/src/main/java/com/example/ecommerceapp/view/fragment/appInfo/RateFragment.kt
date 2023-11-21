package com.example.ecommerceapp.view.fragment.appInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.databinding.FragmentPrivicyBinding
import com.example.ecommerceapp.databinding.FragmentRateBinding
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call

class RateFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentRateBinding
    private lateinit var viewModelUser: ViewModelUserAndPostRequests
    private var rateMessage: String? = null
    private var root: View?=null
    var navController: NavController? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRateBinding.inflate(inflater, container, false)
        root=binding.root

        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        onClick()
        navController = Navigation.findNavController(root!!)
    }

    private fun onClick() {
        binding.scrollView.setVerticalScrollBarEnabled(false)
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        binding.fragmentAddRateReviewsBt.setOnClickListener{
            onValidation()
        }
        binding.backBtn.setOnClickListener{
            onBack()
        }
    }

    private fun onValidation() {
        val editTexts: MutableList<EditText> = java.util.ArrayList()
        val spinners: List<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(binding.fragmentAddRateMessageTil!!)
        var rate = binding.fragmentAddRateRatingBar.rating
        rateMessage = binding.fragmentAddRateMessageEt?.getText().toString()
        Validation.cleanError(textInputLayouts)
        if(rateMessage!!.trim().length==0||rate>0){
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
            if (rate<=0.0){
                return
            }
        }
        if (!Validation.validationLength(
                binding.fragmentAddRateMessageTil,
                getString(R.string.invalid_message),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentAddRateMessageEt!!, requireActivity())
            return
        }
        loginonCall()

    }

    private fun loginonCall() {

        Log.i("eeeeeeeee", rateMessage!!)
        var clientCall: Call<GetRegisterResponse?>? =null
        clientCall = ApiClient.apiClient?.login("phone", rateMessage, BaseActivity.languageToLoad)!!
        viewModelUser!!.setLoginAndRegister(
            generalDataSendedModel!!,clientCall
        )
    }

    private fun initListener() {
        viewModelUser = ViewModelProvider(this).get(ViewModelUserAndPostRequests::class.java)
        viewModelUser!!.loginAndRegister.observe(requireActivity(),
            Observer<GetRegisterResponse?> { response ->
                if (response != null) {
                    if (response.status === true) {
                        ToastCreator.onCreateSuccessToast(requireActivity(), "Success Login")
                        if (BaseActivity.languageToLoad !== "") {
                            HelperMethod.DatePickerFragment.setLocale(
                                requireActivity(),
                                BaseActivity.languageToLoad,
                                2,
                                ""
                            )!!
                        } else {
                            requireActivity()!!.startActivity(
                                Intent(
                                    activity,
                                    HomeCycleActivity::class.java
                                )
                            )
                            requireActivity()!!.finish()
                        }
                        //                        }
                    } else {
                    }
                } else {
                }
            })
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        onValidation()
     }
}