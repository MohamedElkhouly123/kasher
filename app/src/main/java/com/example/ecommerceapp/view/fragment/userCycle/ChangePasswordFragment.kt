package com.example.ecommerceapp.view.fragment.userCycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.databinding.FragmentChangePasswordBinding
import com.example.ecommerceapp.databinding.FragmentMapBinding
import com.example.ecommerceapp.utils.Dialogs.DialogDone
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.utils.validation.Validation.validationConfirmPassword
import com.example.ecommerceapp.utils.validation.Validation.validationPassword
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import retrofit2.Call

class ChangePasswordFragment : BaseFragment() , TryAgainOncall {
    private lateinit var binding: FragmentChangePasswordBinding
    private var root: View? = null
    private var fragmentNewPasswordReNewPassTil: TextInputLayout?=null
    private var fragmentNewPasswordNewPassTil: TextInputLayout?=null
    private var viewModelUser: ViewModelUserAndPostRequests? =null
    private lateinit var password: String
    private var sv: ScrollView?=null
    var navController: NavController? = null

    var fragmentNewPasswordNewPassEt: TextInputEditText? = null

    var fragmentNewPasswordReNewPassEt: TextInputEditText? = null

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
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
//        KeyboardUtil(requireActivity(), root)
        onClick(root!!)
    }



    fun onClick(root: View) {
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.hidenav()
        fragmentNewPasswordNewPassTil=binding.fragmentNewPasswordNewPassTil
        fragmentNewPasswordReNewPassTil=binding.fragmentNewPasswordReNewPassTil
        fragmentNewPasswordNewPassEt=binding.fragmentNewPasswordNewPassEt
        fragmentNewPasswordReNewPassEt=binding.fragmentNewPasswordReNewPassEt
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.userCycleActivity=userCycleActivity
        binding.fragmentEditPasswordSaveBtu.setOnClickListener {
            onValidData()

        }
        binding.backBtn.setOnClickListener {
            onBack()
        }
        binding.fragmentNewPasswordNewPassEt.setOnClickListener { view ->
            binding.fragmentNewPasswordNewPassTil.isErrorEnabled=false
        }
        binding.fragmentNewPasswordNewPassEt.setOnFocusChangeListener { view, hasFocused ->
            binding.fragmentNewPasswordNewPassTil.isErrorEnabled=false
        }
        binding.fragmentNewPasswordReNewPassEt.setOnClickListener { view ->
            binding.fragmentNewPasswordReNewPassTil.isErrorEnabled=false
        }
        binding.fragmentNewPasswordReNewPassEt.setOnFocusChangeListener { view, hasFocused ->
            binding.fragmentNewPasswordReNewPassTil.isErrorEnabled=false
        }
    }

    override fun onBack() {
        super.onBack()
        super.onBack()
    }

    private fun initListener() {
        viewModelUser = ViewModelProvider(this).get(ViewModelUserAndPostRequests::class.java)
        viewModelUser!!.getGeneralAndLogout.observe(requireActivity(),
            Observer<GetGeneralResponse?> { response ->
                if (response != null) {
                    if (response.status === true) {
                        ToastCreator.onCreateSuccessToast(requireActivity(), response.message)
                        val dialog = DialogDone()
                        dialog.showDialog(generalDataSendedModel!!, getString(R.string.success_send))
                    }
                }
            })
    }


    private fun onValidData() {
        val textInputLayoutList: MutableList<TextInputLayout> = ArrayList()
        password = fragmentNewPasswordNewPassEt!!.getText().toString().trim()
        textInputLayoutList.add(fragmentNewPasswordNewPassTil!!)
        textInputLayoutList.add(fragmentNewPasswordReNewPassTil!!)
        Validation.cleanError(textInputLayoutList)
        if(password.length==0||fragmentNewPasswordReNewPassEt!!.getText().toString().trim().length==0){
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        validationPassword(
            fragmentNewPasswordNewPassTil!!,
            8,
            getString(R.string.invalid_password)
        )
        validationPassword(
            fragmentNewPasswordReNewPassTil!!,
            8,
            getString(R.string.invalid_password)
        )
        validationConfirmPassword(
            requireActivity(),
            fragmentNewPasswordNewPassTil!!,
            fragmentNewPasswordReNewPassTil!!
        )
        if (!validationPassword(
                fragmentNewPasswordNewPassTil!!,
                8,
                getString(R.string.invalid_password)
            )
        ) {
            HelperMethod.editCode(fragmentNewPasswordNewPassEt!!, requireActivity())
            return
        }

        if (!validationConfirmPassword(
                requireActivity(),
                fragmentNewPasswordNewPassTil!!,
                fragmentNewPasswordReNewPassTil!!
            )
        ) {
            HelperMethod.editCode(fragmentNewPasswordReNewPassEt!!, requireActivity())
            return
        }
        onCall()
    }

    private fun onCall() {
        var resetPasswordCall: Call<GetGeneralResponse?>? = null
        resetPasswordCall = ApiClient.apiClient?.userResetPassword(password)
        viewModelUser!!.setGeneralAndLogout(generalDataSendedModel!!,
            resetPasswordCall!!
        )

    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        onValidData()
    }
}