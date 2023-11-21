package com.example.ecommerceapp.view.fragment.appInfo

import android.os.Bundle
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
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.databinding.FragmentContactUsBinding
import com.example.ecommerceapp.utils.Dialogs.DialogDone
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call


class ContactUsFragment : BaseFragment(), TryAgainOncall {
    private lateinit var binding: FragmentContactUsBinding
    private var viewModelUser: ViewModelUserAndPostRequests?=null
    private lateinit var messageDetails: String
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var subject: String
    private var root: View?=null
    var navController: NavController? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null

//    @BindView(R.id.fragment_register_last_name_edit)
//    var fragmentRegisterLastNameEdit: TextInputEditText? = null
//
//    @BindView(R.id.fragment_register_email_edit)
//    var fragmentRegisterEmailEdit: TextInputEditText? = null
//
//    @BindView(R.id.fragment_register_phone_number_edit)
//    var fragmentRegisterPhoneNumberEdit: TextInputEditText? = null
//
//    @BindView(R.id.fragment_register_make_acount_btu)
//    var fragmentRegisterMakeAcountBtu: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        setUpActivity()
        navController = Navigation.findNavController(root!!)
        initData()

//        KeyboardUtil(requireActivity(), root!!)
//        ButterKnife.bind(this, root!!)
    }

    private fun initData() {
        homeCycleActivity?.hidenav()
//        ToastCreator.onCreateSuccessToastFull(requireActivity()!!, getString(R.string.invalid_subject))
        onClick()
    }

    fun onClick() {
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.remember=true
        generalDataSendedModel!!.appLanguage=null
        generalDataSendedModel!!.homeCycleActivity=homeCycleActivity
        binding.backBtn.setOnClickListener{
            super.onBack()
        }
        binding.fragmentContactUsMakeAcountBtn.setOnClickListener{
            onValidation()
        }
    }

    private fun onValidation() {
        val editTexts: List<EditText> = java.util.ArrayList()
        val spinners: List<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(binding.fragmentContactUsSubjectTil!!)
        textInputLayouts.add(binding.fragmentContactUsPhoneNumberTil!!)
        textInputLayouts.add(binding.fragmentContactUsEmailTil!!)
        textInputLayouts.add(binding.fragmentContactUsLastNameTil!!)
        textInputLayouts.add(binding.fragmentContactUsMessageDetailsTil!!)

        subject = binding.fragmentContactUsSubjectEdit?.getText().toString()
        phone = binding.fragmentContactUsPhoneNumberEdit?.getText().toString().trim()
        email = binding.fragmentContactUsEmailEdit?.getText().toString()
        name = binding.fragmentContactUsLastNameEdit?.getText().toString().trim()
        messageDetails = binding.fragmentContactUsMessageDetailsEdit?.getText().toString()

        Validation.cleanError(textInputLayouts)
        if (!Validation.validationAllEmpty(
                editTexts,
                textInputLayouts,
                spinners,
                getString(R.string.empty)
            )||messageDetails.trim().length==0
        ) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        Validation.validationLength(
            binding.fragmentContactUsLastNameTil,
            getString(R.string.invalid_full_name),
            2
        )
        Validation.validationLength(
            binding.fragmentContactUsPhoneNumberTil,
            getString(R.string.invalid_phone1),
            2
        )
        Validation.validationEmail(
            requireActivity(),
            binding.fragmentContactUsEmailTil!!
        )
        Validation.validationLength(
            binding.fragmentContactUsSubjectTil,
            getString(R.string.invalid_subject),
            2
        )
        Validation.validationLength(
            binding.fragmentContactUsMessageDetailsTil,
            getString(R.string.invalid_message),
            2
        )
        if (!Validation.validationLength(
                binding.fragmentContactUsLastNameTil,
                getString(R.string.invalid_full_name),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentContactUsLastNameEdit, requireActivity())

            return
        }
        if (!Validation.validationLength(
                binding.fragmentContactUsPhoneNumberTil,
                getString(R.string.invalid_phone1),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentContactUsPhoneNumberEdit, requireActivity())

            return
        }

        if (!Validation.validationEmail(
                requireActivity(),
                binding.fragmentContactUsEmailTil!!
            )
        ) {
            HelperMethod.editCode(binding.fragmentContactUsEmailEdit, requireActivity())

            return
        }




        if (!Validation.validationLength(
                binding.fragmentContactUsSubjectTil,
                getString(R.string.invalid_subject),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentContactUsSubjectEdit, requireActivity())
            return
        }

        if (!Validation.validationLength(
                binding.fragmentContactUsMessageDetailsTil,
                getString(R.string.invalid_message),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentContactUsMessageDetailsEdit, requireActivity())
            return
        }



        contactusOnCall()




    }


    private fun contactusOnCall() {

//        Log.i("eeeeeeeee", phone)
        var clientCall: Call<GetRegisterResponse?>? =null
        clientCall = ApiClient.apiClient?.login(phone, name, BaseActivity.languageToLoad)!!
        viewModelUser!!.setLoginAndRegister(
            generalDataSendedModel!!,clientCall
        )
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

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        onValidation()
    }
}