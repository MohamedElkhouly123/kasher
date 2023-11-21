package com.example.ecommerceapp.view.fragment.userCycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient.apiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.databinding.FragmentConfirmCodeBinding
import com.example.ecommerceapp.databinding.FragmentForgetPasswordBinding
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation.cleanError
import com.example.ecommerceapp.utils.validation.Validation.validationEmail
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import retrofit2.Call

class ForgetPasswordFragment : BaseFragment() , TryAgainOncall {
    private lateinit var binding: FragmentForgetPasswordBinding
    private var root: View?=null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private lateinit var email: String
    private lateinit var viewModelUser: ViewModelUserAndPostRequests
    var navController: NavController? = null

    var fragmentForgetPasswordEmailEdit: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
//        KeyboardUtil(requireActivity(), root)
        onClick(root!!)
    }

    override fun onStart() {
        super.onStart()
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

//        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    private fun initListener() {
        viewModelUser = ViewModelProvider(this).get(ViewModelUserAndPostRequests::class.java)
        viewModelUser.getGeneralAndLogout.observe(requireActivity(),
            Observer<GetGeneralResponse?> { response ->
                if (response != null) {
                    if (response.status === true) {
                        ToastCreator.onCreateSuccessToast(requireActivity(), response.message)
                        navController!!.navigate(R.id.confirmCodeFragment)
                        //                        showToast(getActivity(), "success");
                    }
                }
            })
    }

    fun onClick(root: View) {
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.hidenav()
        fragmentForgetPasswordEmailEdit=binding.fragmentForgetPasswordEmailEdit
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.userCycleActivity=userCycleActivity
        binding.fragmentForgetPasswordSendCodeBtu.setOnClickListener { view ->
//            root!!.forgetPasswordFragment_padding.visibility = View.VISIBLE

            onValidData()
        }
        binding.backBtn.setOnClickListener { view ->
            super.onBack()
        }
        binding.fragmentForgetPasswordEmailEdit.setOnFocusChangeListener { view, hasFocused ->

        }
    }

    private fun onValidData() {
        val textInputLayoutList: MutableList<TextInputLayout> = ArrayList()
        email = fragmentForgetPasswordEmailEdit!!.getText().toString().trim()

        cleanError(textInputLayoutList)
        validationEmail(requireActivity(), binding.fragmentForgetPasswordEmailTil!!)
        if (email.length==0||!validationEmail(requireActivity(), binding.fragmentForgetPasswordEmailTil!!)) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
            HelperMethod.editCode(fragmentForgetPasswordEmailEdit!!, requireActivity())
            return
        }
        navController!!.navigate(R.id.confirmCodeFragment)
        onCall()
    }

    private fun onCall() {
        var resetPasswordCall: Call<GetGeneralResponse?>? = null
        resetPasswordCall = apiClient?.userResetPassword(email)
        viewModelUser.setGeneralAndLogout(generalDataSendedModel!!,
            resetPasswordCall!!
        )

    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        onValidData()
    }

}