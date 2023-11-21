package com.example.ecommerceapp.view.fragment.userCycle

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.databinding.FragmentChangePasswordBinding
import com.example.ecommerceapp.databinding.FragmentConfirmCodeBinding
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.editCode
import com.example.ecommerceapp.utils.HelperMethod.showToast
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import org.aviran.cookiebar2.CookieBar
import retrofit2.Call
import java.util.regex.Pattern


class ConfirmCodeFragment : BaseFragment(), TryAgainOncall {
    private lateinit var smsBroadcastReceiver: BroadcastReceiver
    private lateinit var fragmentVerificationCode3Etx: TextInputEditText
    private lateinit var fragmentVerificationCode1Etx: TextInputEditText
    private lateinit var binding: FragmentConfirmCodeBinding
    private var root: View?=null
    private lateinit var actionType: String
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var confirmCode: String? = null
    private var viewModelUser: ViewModelUserAndPostRequests? =null

    private lateinit var countDownTimer: CountDownTimer
    var totalTime = 60000 // 30 seconds in milliseconds
    var interval = 1000 // 1 second interval
    private var clearEditTextFocused: Boolean=false

    var fragmentVerificationCode4Etx: TextInputEditText? = null



    var fragmentVerificationCode2Etx: TextInputEditText? = null


    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmCodeBinding.inflate(inflater, container, false)
        root=binding.root

        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            setUpActivity()
            initUi()
            onClick(root!!)
        }catch(e :Exception){

        }
    }

    private fun initListener() {
        viewModelUser = ViewModelProvider(this).get(ViewModelUserAndPostRequests::class.java)
        viewModelUser!!.getGeneralAndLogout.observe(requireActivity(),
            Observer<GetGeneralResponse?> { response ->
                if (response != null) {
                    if (response.status === true) {
                        ToastCreator.onCreateSuccessToast(requireActivity(), response.message)
                        navController!!.navigate(R.id.changePasswordFragment)
                        //                        showToast(getActivity(), "success");
                    }
                }
            })
    }

    private fun initUi() {
        navController = Navigation.findNavController(root!!)
        homeCycleActivity?.hidenav()
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.userCycleActivity=userCycleActivity
        fragmentVerificationCode1Etx=binding.fragmentVerificationCode1Etx
        fragmentVerificationCode2Etx=binding.fragmentVerificationCode2Etx
        fragmentVerificationCode3Etx=binding.fragmentVerificationCode3Etx
        fragmentVerificationCode4Etx=binding.fragmentVerificationCode4Etx
        editCode(binding.fragmentVerificationCode1Etx, requireActivity())
        binding.scrollView.setVerticalScrollBarEnabled(false)

        startSmsRetriever(requireActivity())
        startCounter()
        binding.fragmentVerificationCode1Etx.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.fragmentVerificationCode1Etx!!.text.toString().trim().length > 0) {
                    editCode(binding.fragmentVerificationCode2Etx, requireActivity())
                }else{
                    binding.fragmentVerificationCode1Etx.setBackgroundResource(R.drawable.base_focus)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.fragmentVerificationCode2Etx.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.fragmentVerificationCode2Etx.text.toString().trim().length > 0) {

                    editCode(binding.fragmentVerificationCode3Etx, requireActivity())
                }else{
                    binding.fragmentVerificationCode2Etx.setBackgroundResource(R.drawable.base_focus)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.fragmentVerificationCode3Etx.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.fragmentVerificationCode3Etx.text.toString().trim().length > 0) {

                    editCode(binding.fragmentVerificationCode4Etx, requireActivity())
                }else{
                    binding.fragmentVerificationCode3Etx.setBackgroundResource(R.drawable.base_focus)

                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.fragmentVerificationCode4Etx.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (binding.fragmentVerificationCode4Etx.text.toString().trim().length > 0) {
                    binding.fragmentVerificationCode4Etx.clearFocus()
                    val inputMethodManager =requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                    onValidData()
                }else{
                    binding.fragmentVerificationCode4Etx.setBackgroundResource(R.drawable.base_focus)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    private fun startCounter() {
        countDownTimer = object : CountDownTimer(totalTime.toLong(), interval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                // Update UI with remaining time
                val seconds = millisUntilFinished / 1000
                if (seconds >= 1) {
                    try {
                        binding.fragmentConfirmCodeResendCode2Btu.text =
                            getString(R.string.resend_in) + "  " + seconds
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFinish() {
                try {
                    // Do something when countdown is finished
                    binding.fragmentConfirmResendCodeBtu.visibility = View.VISIBLE
                    binding.fragmentConfirmCodeResendCode2Btu.visibility = View.GONE
//                    binding.fragmentConfirmCodeResendCode2Btu.text = getString(R.string.resend)
                } catch (e: Exception) {
                }
            }
        }
        countDownTimer!!.start()
    }

    override fun onStart() {
        super.onStart()
        countDownTimer!!.start()
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            if (smsBroadcastReceiver != null) {
                requireActivity().unregisterReceiver(smsBroadcastReceiver)
            }
            // Stop the countdown timer
            countDownTimer!!.cancel()
        }catch (e:java.lang.Exception){}
    }
    // طول الرمز المستلم
    val SMS_CODE_LENGTH = 4

    // بدء استخدام SMS Retriever API
    fun startSmsRetriever(activity: Activity) {
        // إنشاء كائن SmsRetrieverClient
        val client = SmsRetriever.getClient(activity)
        // بدء الاستماع للرسائل النصية الواردة
        val task = client.startSmsRetriever()
        // إضافة مستمعين لمعالجة النتائج
        task.addOnSuccessListener(object : OnSuccessListener<Void> {
            override fun onSuccess(aVoid: Void?) {
                // تم بدء الاستماع بنجاح
            }
        })
        task.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(e: Exception) {
                // فشل البدء في الاستماع
            }
        })
        // إضافة مستمع للردود (BroadcastReceiver) لمعالجة الرسائل النصية الواردة
        smsBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
                    when (status?.statusCode) {
                        SmsRetrieverStatusCodes.SUCCESS -> {
                            // تم العثور على الرسالة النصية المطلوبة
                            val message = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String
                            if (message != null) {
                                showToast(activity, ""+message)
                                // استخراج الرمز التحقق من الرسالة النصية
                                val pattern = Pattern.compile("\\d{$SMS_CODE_LENGTH}")
                                val matcher = pattern.matcher(message)
                                if (matcher.find()) {
                                    val smsCode = matcher.group(0)
                                    // نقل الرمز التحقق إلى الدالة المحددة
//                                    onSmsCodeReceived(smsCode)
                                }
                            }
                        }
                        SmsRetrieverStatusCodes.TIMEOUT -> {
                            // انتهت صلاحية الرسالة النصية
                        }
                        else -> {
                            // فشل في استلام الرسالة النصية
                        }
                    }
                }
            }
        }
        // تسجيل المستمع للردود (BroadcastReceiver)
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity.registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    fun onClick(root: View) {


        binding.backBtn.setOnClickListener {
                super.onBack()
            }
            binding.fragmentConfirmResendCodeBtu.setOnClickListener {
                actionType="resend"
                resendOnCall()
                startCounter()
                binding.fragmentConfirmResendCodeBtu.visibility = View.GONE
                binding.fragmentConfirmCodeResendCode2Btu.visibility = View.VISIBLE
            }
        binding.fragmentConfirmContinueBtu.setOnClickListener {
            actionType="confirm"
            onValidData()
            navController!!.navigate(R.id.changePasswordFragment)
        }

        binding.fragmentVerificationCode1Etx.setOnTouchListener { view, event ->
            binding.fragmentVerificationCode1Etx.isEnabled  = true
            clearEditTextFocused=true
            false // return false to allow other listeners to process the event
        }
        binding.fragmentVerificationCode2Etx.setOnTouchListener { view, event ->
            binding.fragmentVerificationCode2Etx.isEnabled  = true
            clearEditTextFocused=true
            false // return false to allow other listeners to process the event
        }
        binding.fragmentVerificationCode3Etx.setOnTouchListener { view, event ->
            binding.fragmentVerificationCode3Etx.isEnabled  = true
            clearEditTextFocused=true
            false // return false to allow other listeners to process the event
        }
        binding.fragmentVerificationCode4Etx.setOnTouchListener { view, event ->
            binding.fragmentVerificationCode4Etx.isEnabled  = true
            clearEditTextFocused=true
            false // return false to allow other listeners to process the event
        }
        binding.fragmentVerificationCode1Etx.setOnFocusChangeListener { view, hasFocused ->
            checkFocus(hasFocused,binding.fragmentVerificationCode1Etx)
        }
        binding.fragmentVerificationCode2Etx.setOnFocusChangeListener { view, hasFocused ->
            if (binding.fragmentVerificationCode1Etx.text?.isEmpty() == true){
                editCode(binding.fragmentVerificationCode1Etx, requireActivity())
                binding.fragmentVerificationCode2Etx.isEnabled=false
            }else {
                checkFocus(hasFocused, binding.fragmentVerificationCode2Etx)
            }
        }
        binding.fragmentVerificationCode3Etx.setOnFocusChangeListener { view, hasFocused ->
            if (binding.fragmentVerificationCode2Etx.text?.isEmpty() == true){
                editCode(binding.fragmentVerificationCode2Etx, requireActivity())
                binding.fragmentVerificationCode3Etx.isEnabled=false
            }else {
                checkFocus(hasFocused, binding.fragmentVerificationCode3Etx)
            }
        }
        binding.fragmentVerificationCode4Etx.setOnFocusChangeListener { view, hasFocused ->
            if (binding.fragmentVerificationCode3Etx.text?.isEmpty() == true){
                editCode(binding.fragmentVerificationCode3Etx, requireActivity())
                binding.fragmentVerificationCode4Etx.isEnabled=false
            }else {
                checkFocus(hasFocused, binding.fragmentVerificationCode4Etx)
            }
        }


    }
    private fun checkFocus(hasFocused: Boolean, fragmentVerificationCodeEtx: TextInputEditText) {
        binding.fragmentConfirmCodeSendCodeErrorTv.visibility=View.GONE
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            fragmentVerificationCodeEtx.setTextCursorDrawable(R.color.app_color)
//        }
        if (hasFocused&&clearEditTextFocused){
            fragmentVerificationCodeEtx.setText("")
//            fragmentVerificationCodeEtx.setTextColor(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.app_color
//                )
//            )
            fragmentVerificationCodeEtx.setBackgroundResource(R.drawable.base_focus)
            clearEditTextFocused=false
        }else {
            if (fragmentVerificationCodeEtx.text?.isEmpty() == true) {
                fragmentVerificationCodeEtx.setBackgroundResource(R.drawable.base_normal)
//                fragmentVerificationCodeEtx.setTextColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.app_color
//                    )
//                )
            } else {
                textEditFinish(fragmentVerificationCodeEtx)
            }
        }
    }

    private fun textEditFinish(fragmentVerificationCodeEtx: TextInputEditText) {
        fragmentVerificationCodeEtx.setBackgroundResource(R.drawable.base_selected_edited)
//        fragmentVerificationCodeEtx.setTextColor(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.white
//            )
//        )
    }

    private fun showError() {
        binding.fragmentConfirmCodeSendCodeErrorTv.visibility=View.VISIBLE
        setError(binding.fragmentVerificationCode1Etx)
        setError(binding.fragmentVerificationCode2Etx)
        setError(binding.fragmentVerificationCode3Etx)
        setError(binding.fragmentVerificationCode4Etx)
    }

    private fun setError(fragmentVerificationCodeEtx: TextInputEditText) {
        fragmentVerificationCodeEtx.setText("")
        fragmentVerificationCodeEtx.setBackgroundResource(R.drawable.base_error)
    }

    private fun onValidData() {
        showError()
        confirmCode =
                    fragmentVerificationCode1Etx!!.getText().toString()+
                    fragmentVerificationCode2Etx!!.getText().toString()+
                    fragmentVerificationCode3Etx!!.getText().toString()+
                    fragmentVerificationCode4Etx!!.getText().toString()
        if(confirmCode!!.trim().length<4){
            HelperMethod.showCookieMsg(
                getString(R.string.warning), getString(R.string.insert_code_help),
                activity, R.color.red2, CookieBar.TOP
            )
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
            return
        }
        onCall()
    }

    private fun onCall() {
        var resetPasswordCall: Call<GetGeneralResponse?>? = null
        resetPasswordCall = ApiClient.apiClient?.userResetPassword(confirmCode)
        viewModelUser!!.setGeneralAndLogout(generalDataSendedModel!!,
            resetPasswordCall!!
        )

    }

    private fun resendOnCall() {
        var resetPasswordCall: Call<GetGeneralResponse?>? = null
        resetPasswordCall = ApiClient.apiClient?.userResetPassword(confirmCode)
        viewModelUser!!.setGeneralAndLogout(generalDataSendedModel!!,
            resetPasswordCall!!
        )

    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        if("resend".equals(actionType)){
            resendOnCall()
        }else {
            onValidData()
        }
    }
}