package com.example.ecommerceapp.view.fragment.userCycle

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient.apiClient
import com.example.ecommerceapp.data.local.SharedPreferencesManger.GOOGLECHECK
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveData
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.databinding.FragmentLoginBinding
import com.example.ecommerceapp.utils.Dialogs.ShowBottomSheetCountryRecycleList
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.editCode
import com.example.ecommerceapp.utils.HelperMethod.filterInEditText
import com.example.ecommerceapp.utils.HelperMethod.replaceFragment
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.utils.validation.Validation.cleanError
import com.example.ecommerceapp.utils.validation.Validation.validationLengthClearPhoneError
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity.Companion.languageToLoad
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call


class LoginFragment : BaseFragment(), TryAgainOncall, MakeChangeInFragment {
    private lateinit var bundle: Bundle
    private var googleCheck = "true"
    private lateinit var googleSignInClient: GoogleSignInClient
    private var validationStarted: Boolean=false
    private var phoneMustBeLength: Int=7
    private lateinit var showBottomSheetCountryRecycleList: ShowBottomSheetCountryRecycleList
    private lateinit var binding: FragmentLoginBinding
    private var root: View?=null
    private var fragmentLoginPasswordEdt: TextInputEditText?=null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var fragmentLoginPhoneNumberTil: TextInputLayout?=null
    private var fragmentLoginPasswordTil: TextInputLayout?=null
    private lateinit var phone: String
    private lateinit var password: String
    var editTextsList: List<EditText> = ArrayList()

    var fragmentLoginPhoneNumberEt: TextInputEditText? = null
    private lateinit var generalBundelDataToSend: GeneralBundelDataToSend


    private val bottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var viewModelUser: ViewModelUserAndPostRequests? = null
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setUpUserActivity()
        setUpActivity()
        initData(root!!)
        refreshLanguage()
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

    private fun initData(root: View) {
        navController = Navigation.findNavController(root)
        homeCycleActivity?.hidenav()
        onClick(root)
        fragmentLoginPasswordEdt?.setTypeface(Typeface.DEFAULT)
        fragmentLoginPasswordEdt?.setTransformationMethod(PasswordTransformationMethod())
        bundle=Bundle()

        generalBundelDataToSend= GeneralBundelDataToSend()

    }

    override fun onResume() {
        super.onResume()
        binding.fragmentLoginPasswordEt.setOnClickListener { view ->
            binding.appBarLayout.setExpanded(false)
            binding.fragmentLoginPasswordTil.isErrorEnabled=false
        }
        binding.fragmentLoginPasswordEt.setOnFocusChangeListener { view, hasFocused ->
            binding.appBarLayout.setExpanded(false)
            binding.fragmentLoginPasswordTil.isErrorEnabled=false
        }
    }

    override fun onStop() {
        super.onStop()
//        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun onClick(root: View) {
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.remember=true
        generalDataSendedModel!!.appLanguage=null
        generalDataSendedModel!!.userCycleActivity=userCycleActivity
        fragmentLoginPhoneNumberTil=binding.fragmentLoginPhoneNumberOrEmailTil
        fragmentLoginPasswordTil=binding.fragmentLoginPasswordTil
        fragmentLoginPasswordEdt=binding.fragmentLoginPasswordEt
        generalDataSendedModel!!.makeChangeInFragment=this
        binding.scrollView.setVerticalScrollBarEnabled(false)
        binding.fragmentLoginForgetPassTv!!.setOnClickListener {
//            replaceFragment(
//                requireActivity()!!.supportFragmentManager,
//                R.id.user_frame,
//                ForgetPasswordFragment()
//            )
            navController!!.navigate(R.id.forgetPasswordFragment)
            }
        binding.fragmentLoginSiginEt!!.setOnClickListener {
//            replaceFragment(
//                requireActivity()!!.supportFragmentManager,
//                R.id.user_frame,
//                RegisterFragment()
//            )
            navController!!.navigate(R.id.RegisterFragment)
        }

        binding.backBtu!!.setOnClickListener { onBack() }

        binding.fragmentLoginLoginBtu!!.setOnClickListener {
            onValidation()
        }
        showBottomSheetCountryRecycleList= ShowBottomSheetCountryRecycleList(generalDataSendedModel)

        binding.fragmentmLoginCodeSelectAutoComplete.setOnClickListener{
            showBottomSheetCountryRecycleList.showBottomSheetDialog()
        }
        binding.fragmentmLoginSelectAutoCompleteCountryImg.setOnClickListener{
            showBottomSheetCountryRecycleList.showBottomSheetDialog()
        }
        binding.fragmentLoginConinueWithGoogleBtn.setOnClickListener{
            try {
                initGoogleSignInView()

            } catch (e: Exception) {
            }
        }
        filterInEditText(binding.fragmentLoginPhoneNumberEt,R.string.please_enter_english_letters_or_numbers,"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@._-", generalDataSendedModel!!)
    }



    private fun initGoogleSignInView() {
        HelperMethod.progressDialog = ProgressDialog(activity)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity()!!, gso)
        googleSignInClient!!.signOut()
        googleCheck = "false"
        SaveData(requireActivity(), GOOGLECHECK, googleCheck)
        //        editor = sharedPref.edit();
//        editor.putString("value", googleCheck);
//        editor.commit();
        HelperMethod.progressDialog?.setMessage(requireActivity().getString(R.string.signing_in))
        HelperMethod.progressDialog?.show()
        val signInIntent: Intent = googleSignInClient!!.getSignInIntent()
        startActivityForResult(signInIntent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        Toast.makeText(getContext(),"e()"+requestCode, Toast.LENGTH_SHORT).show();
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> try {
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
//        if (resultCode == Activity.RESULT_OK) {

//            if (requestCode === 101) {
////            try {
//                // The Task returned from this call is always completed, no need to attach
//                // a listener.
//
//                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//                val account = task.getResult(
//                    ApiException::class.java
//                )
//                onLoggedInByGoogle(account)
//                Toast.makeText(getContext(),"e()", Toast.LENGTH_SHORT).show();
////            } catch (e: ApiException) {
////                // The ApiException status code indicates the detailed failure reason.
////                Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
////            }
////        }
//
//
//        }
    }

    private fun updateUI() {
        generalBundelDataToSend.from="phoneRequired"
        bundle.putSerializable("generalBundelDataToSend", generalBundelDataToSend)
        val intent = Intent(requireContext(), HomeCycleActivity::class.java)
        if(generalBundelDataToSend!=null) {
            bundle.putSerializable("generalBundelDataToSend", generalBundelDataToSend)
            intent.putExtras(bundle)
        }
        startActivity(intent)
        requireActivity().finish()
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
        val spinners: List<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(binding.fragmentLoginPasswordTil!!)
        textInputLayouts.add(binding.fragmentLoginPhoneNumberOrEmailTil!!)
        password = fragmentLoginPasswordEdt?.getText().toString()
        phone = fragmentLoginPhoneNumberEt?.getText().toString().trim()
        cleanError(textInputLayouts)
        if (!Validation.validationAllEmpty(
                editTexts,
                textInputLayouts,
                spinners,
                getString(R.string.empty)
            )
        ) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        val isDigitsOnly = TextUtils.isDigitsOnly(binding.fragmentLoginPhoneNumberEt.text.toString())
        if(isDigitsOnly) {
            validationLengthClearPhoneError(
                binding.fragmentLoginPhoneNumberOrEmailTil,
                getString(R.string.invalid_phone_not_correct),
                phoneMustBeLength
            )
        }else{
            Validation.validationEmail(requireActivity(), binding.fragmentLoginPhoneNumberOrEmailTil)

        }
        Validation.validationPassword(
            fragmentLoginPasswordTil!!,
            8,
            getString(R.string.invalid_password)
        )
        if(isDigitsOnly) {

        if (!validationLengthClearPhoneError(binding.fragmentLoginPhoneNumberOrEmailTil, getString(R.string.invalid_phone_not_correct), phoneMustBeLength)) {
            editCode(binding.fragmentLoginPhoneNumberEt!!, requireActivity())
            return
        }
        }else{
            if (!Validation.validationEmail(requireActivity(), binding.fragmentLoginPhoneNumberOrEmailTil)) {
                HelperMethod.editCode(binding.fragmentLoginPhoneNumberEt!!, requireActivity())
                return
            }
        }
        if (!Validation.validationPassword(
                fragmentLoginPasswordTil!!,
                8,
                getString(R.string.invalid_password)
            )
        ) {
            editCode(fragmentLoginPasswordEdt!!, requireActivity())
            return
        }


//        if (!Validation.validationPassword(requireActivity(),fragmentLoginPasswordTil?.getEditText()?.getText().toString(),7 ,getString(R.string.invalid_password))) {
//        showCookieMsg(getString(R.string.warning), getString(R.string.invalid_password),requireActivity(), R.color.red2, CookieBar.TOP)
//        return
//          }

        loginonCall()

    }

//    override fun onBack() {
//       requireActivity().finish()
//     }


    private fun loginonCall() {

        Log.i("eeeeeeeee", phone)
        var clientCall: Call<GetRegisterResponse?>? =null
        generalDataSendedModel!!.password=password
        clientCall = apiClient?.login(phone, password, languageToLoad)!!
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
                        requireActivity().finish()
//                        if (languageToLoad !== "") {
//                            setLocale(requireActivity(), languageToLoad, 2, "")!!
//                        } else {
//                            val intent = Intent(context, HomeCycleActivity::class.java)
//                            val bundle = Bundle()
////                            if (appLink != null && appLink !== "") {
////                                bundle.putSerializable("appLink", appLink)
////                                intent.putExtras(bundle)
////                            }
////                            requireActivity()!!.startActivity(
////                                Intent(
////                                    activity,
////                                    HomeCycleActivity::class.java
////                                )
////                            )
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
    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
            onValidation()
     }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        phoneMustBeLength = generalDataSendedModel.countryItem.countryValidNumber
        this.generalDataSendedModel = generalDataSendedModel
        if ("Change Country".equals(generalDataSendedModel.type)) {
            showBottomSheetCountryRecycleList.dismiss()
            binding.fragmentmEnterPhoneNumberCodeTv.text =
                generalDataSendedModel.countryItem.countryCodeWithPlus
            binding.fragmentmLoginSelectAutoCompleteCountryImg.setImageResource(
                generalDataSendedModel.countryItem.countryImge
            )
            if (validationStarted) {
                Validation.validationLengthClearPhoneError(
                    binding.fragmentLoginPhoneNumberOrEmailTil,
                    getString(R.string.invalid_phone_not_correct),
                    phoneMustBeLength
                )
                binding.fragmentLoginPhoneNumberOrEmailTil.isErrorEnabled = false
            }
        }
    }
}