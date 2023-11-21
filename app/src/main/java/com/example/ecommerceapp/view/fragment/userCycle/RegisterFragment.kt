package com.example.ecommerceapp.view.fragment.userCycle

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.databinding.FragmentRegisterBinding
import com.example.ecommerceapp.utils.Dialogs.ShowBottomSheetCountryRecycleList
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.openGoogleCroomLink
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.utils.validation.Validation.cleanError
import com.example.ecommerceapp.utils.validation.Validation.validationAllEmpty
import com.example.ecommerceapp.utils.validation.Validation.validationEmail
import com.example.ecommerceapp.utils.validation.Validation.validationLength
import com.example.ecommerceapp.utils.validation.Validation.validationPassword
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout

import retrofit2.Call


class RegisterFragment : BaseFragment(), TryAgainOncall, MakeChangeInFragment {
    private lateinit var navController: NavController
    private lateinit var generalBundelDataToSend: GeneralBundelDataToSend
    private var phoneMustBeLength: Int=7
    private lateinit var showBottomSheetCountryRecycleList: ShowBottomSheetCountryRecycleList
    private lateinit var binding: FragmentRegisterBinding
    private val termsUrl: String? = "https://privacy-policy.flash-book.net/ar/user_terms&conditions.html"
    private lateinit var name: String
    private lateinit var email: String
    private var phone: String? = null
    private var password: String? =null
    private var viewModelUser: ViewModelUserAndPostRequests? =null
    private var isCustomer: Boolean=false
    private var isTrade: Boolean=false
    private var first: Boolean=true
    private var notEmpty: Boolean=false
    private lateinit var imm: InputMethodManager
    private var root: View? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var validationStarted: Boolean=false
    private lateinit var bundle: Bundle
    private var googleCheck = "true"
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        onClick()
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            requireActivity().window.statusBarColor = Color.TRANSPARENT
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    fun onClick() {
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.hidenav()
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.remember=true
        generalDataSendedModel!!.appLanguage=null
        generalDataSendedModel!!.userCycleActivity=userCycleActivity
        generalDataSendedModel!!.makeChangeInFragment=this
        binding.scrollView.setVerticalScrollBarEnabled(false)
        bundle=Bundle()
        generalBundelDataToSend= GeneralBundelDataToSend()
        showBottomSheetCountryRecycleList= ShowBottomSheetCountryRecycleList(generalDataSendedModel)
        binding.fragmentmRegisterEnterPhoneNumberCodeSelectAutoComplete.setOnClickListener{
            showBottomSheetCountryRecycleList.showBottomSheetDialog()
        }
        binding.fragmentmRegisterEnterPhoneNumberCodeSelectAutoCompleteCountryImg.setOnClickListener{
            showBottomSheetCountryRecycleList.showBottomSheetDialog()
        }
        binding.backBtu.setOnClickListener(View.OnClickListener {
            super.onBack()
        })

        binding.fragmentRegisterTradeLy.setOnClickListener{
            binding.fragmentRegisterCustomerLy!!.setBackgroundColor(
                requireActivity().getResources().getColor(R.color.white)
            )
            binding.fragmentRegisterTradeLy!!.setBackgroundColor(
                requireActivity().getResources().getColor(R.color.app_second_color_sky)
            )
            isCustomer=false
            isTrade=true
        }
         binding.fragmentRegisterCustomerLy.setOnClickListener{
            binding.fragmentRegisterTradeLy!!.setBackgroundColor(
                requireActivity().getResources().getColor(R.color.white)
            )
            binding.fragmentRegisterCustomerLy!!.setBackgroundColor(
                requireActivity().getResources().getColor(R.color.app_second_color_sky)
            )
            isCustomer=true
            isTrade=false
        }
        binding.fragmentRegisterMakeAcountBtn.setOnClickListener {
            onValidation()
        }
        binding.fragmentRegisterConditionsBtn.setOnClickListener {
            generalDataSendedModel?.googleUrl=termsUrl
            openGoogleCroomLink(generalDataSendedModel!!)
        }
        binding.fragmentRegisterSiginBtn.setOnClickListener {
            onBack()
        }

        binding.fragmentRegisterPasswordEdit.setOnFocusChangeListener { view, hasFocused ->
            if(hasFocused){
                binding.fragmentRegisterPasswordTil.isErrorEnabled=false
            }
        }
        binding.fragmentRegisterPasswordEdit.setOnClickListener {
            binding.fragmentRegisterPasswordTil.isErrorEnabled=false
        }
        binding.fragmentRegisterConinueWithGoogleBtn.setOnClickListener{
            try {
                initGoogleSignInView()

            } catch (e: Exception) {
            }
        }
    }


    private fun initGoogleSignInView() {
        HelperMethod.progressDialog = ProgressDialog(activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity()!!, gso)
        googleSignInClient!!.signOut()
        googleCheck = "false"
        SharedPreferencesManger.SaveData(
            requireActivity(),
            SharedPreferencesManger.GOOGLECHECK,
            googleCheck
        )
        //        editor = sharedPref.edit();
//        editor.putString("value", googleCheck);
//        editor.commit();
        HelperMethod.progressDialog?.setMessage(requireActivity().getString(R.string.signing_in))
        HelperMethod.progressDialog?.show()
        val signInIntent: Intent = googleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                101 -> try {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(
                        ApiException::class.java
                    )
                    onLoggedInByGoogle(account)
                } catch (e: ApiException) {
                    // The ApiException status code indicates the detailed failure reason.
                    Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
                }
            }
        }
    }

    private fun updateUI() {
        generalBundelDataToSend.from="phoneRequired"
        bundle.putSerializable("generalBundelDataToSend", generalBundelDataToSend)
//        navController!!.navigate(R.id.createNewAccountFragment,bundle)
    }

    private fun onLoggedInByGoogle(googleSignInAccount: GoogleSignInAccount) {
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//
//        }

        Toast.makeText(getContext(), googleSignInAccount.getId()+"  " + googleSignInAccount.getEmail()+"  " + googleSignInAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
        Log.i("googleSignInAccou", googleSignInAccount.id + "  ")
        getPhone()
        val intent2: Intent = googleSignInClient!!.getSignInIntent()
        startActivityForResult(intent2, Activity.RESULT_OK)
        val words = googleSignInAccount.displayName!!.split (" ")
        val firstName = words.firstOrNull ()
//        generalBundelDataToSend.userData!!.firstname= firstName
//        generalBundelDataToSend.userData!!.lastname=googleSignInAccount.familyName
//        generalBundelDataToSend.userData!!.email=googleSignInAccount.getEmail()
        HelperMethod.progressDialog!!.dismiss()

        updateUI()
//        val clientCall: Call<GetRegisterResponse>
//        UserCycleActivity.userEmail = googleSignInAccount.email
//        clientCall = ApiClient.getApiClient().socialLogin(
//            mac,
//            googleSignInAccount.email,
//            "google",
//            googleSignInAccount.displayName,
//            googleSignInAccount.id,
//            languageToLoad
//        )
//        viewModelUser.setLoginAndRegister(
//            activity,
//            clientCall,
//            { type: String? -> tryAgainCall(type) },
//            password,
//            true,
//            null
//        )


//        Intent intent = new Intent(getActivity(), HomeCycleActivity.class);
//        intent.putExtra("GOOGLE_ACCOUNT", googleSignInAccount);
////        intent.putExtra("gCheck",googleCheck);
//        startActivity(intent);
//        getActivity().finish();
    }
    private fun getPhone() {

//        AccountManager am = AccountManager.get(getActivity());
//        Account[] accounts = am.getAccounts();
//
//        for (Account ac : accounts) {
//            String acname = ac.name;
//            String actype = ac.type;
//            String phoneNumber="";
//            if(actype.equals("com.whatsapp")){
//                phoneNumber = ac.name;
//            }
//        2
        var phoneNumber = ""
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                requireContext()!!,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity()!!,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                val tMgr = requireContext()!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                phoneNumber = tMgr.line1Number
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {


//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        READ_CALL_LOG );
            }
        }
        // Take your time to look at all available accounts
//            Toast.makeText(getActivity(), "Accounts : " + phoneNumber, Toast.LENGTH_SHORT).show();
    }

    private fun onValidation() {
        validationStarted=true
        val editTexts: MutableList<EditText> = java.util.ArrayList()
        val spinners: MutableList<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(binding.fragmentRegisterNameTil!!)
        textInputLayouts.add(binding.fragmentRegisterEmailTil!!)
        textInputLayouts.add(binding.fragmentRegisterPhoneNumberTil!!)
        textInputLayouts.add(binding.fragmentRegisterPasswordTil!!)
        password = binding.fragmentRegisterPasswordEdit.getText().toString()
        phone = binding.fragmentRegisterPhoneNumberEdit?.getText().toString().trim()
        email = binding.fragmentRegisterEmailEdit?.getText().toString()
        name = binding.fragmentRegisterNameEdit?.getText().toString().trim()

        cleanError(textInputLayouts)
        if (!validationAllEmpty(editTexts, textInputLayouts, spinners, getString(R.string.empty))) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        validationLength(
            binding.fragmentRegisterNameTil,
            getString(R.string.invalid_full_name),
            2
        )
        validationEmail(requireActivity(), binding.fragmentRegisterEmailTil)
        validationPassword(
            binding.fragmentRegisterPasswordTil,
            8,
            getString(R.string.invalid_password)
        )
        Validation.validationLengthClearPhoneError(
            binding.fragmentRegisterPhoneNumberTil,
            getString(R.string.invalid_phone_not_correct),
            phoneMustBeLength
        )

        if (!validationLength(
                binding.fragmentRegisterNameTil,
                getString(R.string.invalid_full_name),
                2
            )
        ) {
            HelperMethod.editCode(binding.fragmentRegisterNameEdit!!, requireActivity())
                return
        }

        if (!validationEmail(requireActivity(), binding.fragmentRegisterEmailTil)) {
            HelperMethod.editCode(binding.fragmentRegisterEmailEdit!!, requireActivity())
            return
        }
        if (!validationPassword(
                binding.fragmentRegisterPasswordTil,
                8,
                getString(R.string.invalid_password)
            )
        ) {
            HelperMethod.editCode(binding.fragmentRegisterPasswordEdit!!, requireActivity())
            return
        }
        if (!Validation.validationLengthClearPhoneError(
                binding.fragmentRegisterPhoneNumberTil,
                    getString(R.string.invalid_phone_not_correct),
                phoneMustBeLength
                )
        ) {
                HelperMethod.editCode(binding.fragmentRegisterPhoneNumberEdit!!, requireActivity())
                return
        }

//        if (!isCustomer&&!isTrade) {
//            showCookieMsg(
//                getString(R.string.warning), getString(R.string.check_empty_account_type),
//                activity, R.color.red2, CookieBar.TOP
//            )
//            return
//        }
        if (!binding.fragmentRegisterAgreeConditionCheckbox.isChecked()) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.check_empty_login))
            return
        }
        registerCall()
    }


    private fun registerCall() {

//        Log.i("eeeeeeeee", phone)
        var clientCall: Call<GetRegisterResponse?>? =null
        generalDataSendedModel!!.password=password
        clientCall = ApiClient.apiClient?.login(phone, password, BaseActivity.languageToLoad)!!
        viewModelUser!!.setLoginAndRegister(
            generalDataSendedModel!!,clientCall
        )
    }

    override fun onResume() {
        super.onResume()
//        var isOpened = false
////        Toast.makeText(context, "" + "MATCH_PARENT", Toast.LENGTH_SHORT).show()
//        object : CountDownTimer(2147400000, 5000) {
//            override fun onTick(millisUntilFinished: Long) {
                imm = requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

////                Toast.makeText(context, "" + "MATCH_PARENT"+imm.isAcceptingText, Toast.LENGTH_SHORT).show()
//                }
//
//            override fun onFinish() {
//                TODO("Not yet implemented")
//            }
//
//        }.start()
//
        binding.fragmentRegisterPhoneNumberEdit.setOnClickListener { view ->
            binding.appBarLayout.setExpanded(false)
            binding.fragmentRegisterPhoneNumberTil.isErrorEnabled=false
        }
        binding.fragmentRegisterPhoneNumberEdit.setOnFocusChangeListener { view, hasFocused ->
            binding.appBarLayout.setExpanded(false)
            binding.fragmentRegisterPhoneNumberTil.isErrorEnabled=false
        }

//        root!!.fragment_register_phone_number_edit.setOnFocusChangeListener { view, hasFocused ->
////          if(imm.isAcceptingText) {
//            chechText()
//            if (root!!.fragment_register_password_edit.text?.trim()?.length == 0) {
//                if (hasFocused) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        root!!.fragment_register_password_edit.requestFocus()
//                        imm.showSoftInput(
//                            root!!.fragment_register_password_edit,
//                            InputMethodManager.SHOW_IMPLICIT
//                        );
////                    root!!.fragment_register_phone_number_edit.requestFocus()
////                    imm.showSoftInput(root!!.fragment_register_phone_number_edit, InputMethodManager.SHOW_IMPLICIT);
//
//                    }
////                }
//            } else {
//
//            }
//            if (hasFocused ) {
////                    Toast.makeText(context, "" + root!!.fragment_register_toolbar.height, Toast.LENGTH_SHORT).show()
////                    root!!.rigister_padding_ly.visibility = View.GONE
//            } else {
//
////                    root!!.rigister_padding_ly.visibility = View.VISIBLE
//            }
//
//        }


    }

    private fun initListener() {
        viewModelUser = ViewModelProvider(this).get(ViewModelUserAndPostRequests::class.java)
        viewModelUser!!.loginAndRegister.observe(requireActivity(),
            Observer<GetRegisterResponse?> { response ->
                if (response != null) {
                    if (response.status === true) {
                        ToastCreator.onCreateSuccessToast(requireActivity(), "Success Login")
                        requireActivity().finish()
//                        if (BaseActivity.languageToLoad !== "") {
//                            HelperMethod.DatePickerFragment.setLocale(
//                                requireActivity(),
//                                BaseActivity.languageToLoad,
//                                2,
//                                ""
//                            )!!
//                        } else {
//                            val intent = Intent(context, HomeCycleActivity::class.java)
//                            val bundle = Bundle()
////                            if (appLink != null && appLink !== "") {
////                                bundle.putSerializable("appLink", appLink)
////                                intent.putExtras(bundle)
////                            }
//                            requireActivity()!!.startActivity(
//                                Intent(
//                                    activity,
//                                    HomeCycleActivity::class.java
//                                )
//                            )
//                            activity!!.finish()
//                        }
                        //                        }
                    } else {
//                        Log.i(
//                            "qwwqwqwqwqwqwqwqwqwqwq",
//                            java.lang.String.valueOf(response.getMessage())
//                        )
                    }
                } else {
                }
            })
    }

    private fun chechText() {
        if(binding.fragmentRegisterEmailEdit.text?.trim()?.length!! >0){
            notEmpty=true
        }else{
            notEmpty=false
            return
        }
        if(binding.fragmentRegisterPhoneNumberEdit.text?.trim()?.length!!>0){
            notEmpty=true
        }else{
            notEmpty=false
            return
        }
        if(binding.fragmentRegisterEmailEdit.text?.trim()?.length!!>0){
            notEmpty=true
        }else{
            notEmpty=false
            return
        }
    }


    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
       onValidation()
    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        phoneMustBeLength = generalDataSendedModel.countryItem.countryValidNumber
        this.generalDataSendedModel = generalDataSendedModel
        if ("Change Country".equals(generalDataSendedModel.type)) {
            showBottomSheetCountryRecycleList.dismiss()
            binding.fragmentmRegisterEnterPhoneNumberCodeTv.text =
                generalDataSendedModel.countryItem.countryCodeWithPlus
            binding.fragmentmRegisterEnterPhoneNumberCodeSelectAutoCompleteCountryImg.setImageResource(
                generalDataSendedModel.countryItem.countryImge
            )

            if (validationStarted) {
                Validation.validationLengthClearPhoneError(
                    binding.fragmentRegisterPhoneNumberTil,
                    getString(R.string.invalid_phone_not_correct),
                    phoneMustBeLength
                )
                binding.fragmentRegisterPhoneNumberTil.isErrorEnabled = false
            }
        }
    }
}