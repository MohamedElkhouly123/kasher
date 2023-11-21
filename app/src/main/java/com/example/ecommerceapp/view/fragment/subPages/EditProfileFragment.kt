package com.example.ecommerceapp.view.fragment.subPages

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.api.ApiClient
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.GetGeneralResponse
import com.example.ecommerceapp.data.model.getRegisterResponse.GetRegisterResponse
import com.example.ecommerceapp.databinding.FragmentEditProfileBinding
import com.example.ecommerceapp.databinding.FragmentOrders2Binding
import com.example.ecommerceapp.utils.Dialogs.DialogDone
import com.example.ecommerceapp.utils.HelperMethod.editCode
import com.example.ecommerceapp.utils.HelperMethod.onLoadCirImageFromUrl2
import com.example.ecommerceapp.utils.KeyboardUtil
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.utils.validation.Validation.cleanError
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import retrofit2.Call


class EditProfileFragment : BaseFragment() , TryAgainOncall {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var phone: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var imagePath: String
    private val numberOfImages: Int=4
    private var isChangePassword: Boolean=false
    private var currentPassword: String?="12345678"
    private lateinit var renewPassword: String
    private lateinit var oldPassword: String
    private lateinit var dialogChangePassword: BottomSheetDialog
    private var newPassword: String? = null
    private lateinit var viewModelUser: ViewModelUserAndPostRequests
    private var root: View?=null
    var navController: NavController? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private val READ_REQUEST_CODE = 42
    private val mimeTypes: ArrayList<String> = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        navController = Navigation.findNavController(root!!)
//        KeyboardUtil(requireActivity(), root!!)
        initData(root!!)
    }

    private fun initData(root: View) {
        homeCycleActivity?.hidenav()
        imagePath=""
        name=""
        email=""
        password=""
        phone=""
        binding.fragmentEditProfileFirstNameEdit.setText("data")
        binding.fragmentEditProfilePasswordEdit.setText("data")
        binding.fragmentEditProfilePhoneNumberEdit.setText("data")
        binding.fragmentEditProfileEmailEdit.setText("data")
        onClick(root)
    }

    fun onClick(root: View) {
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.remember=true
        generalDataSendedModel!!.appLanguage=null
        generalDataSendedModel!!.homeCycleActivity=homeCycleActivity
        binding.fragmentEditProfileFirstNameStartEditBtn.setOnClickListener {
//            showToast(activity, getString(R.string.startNameEdit))
            binding.fragmentEditProfileFirstNameStartEditBtn.visibility=View.GONE
            makeEditTextFoucasable(binding.fragmentEditProfileFirstNameEdit)
            editCode(binding.fragmentEditProfileFirstNameEdit!!, requireActivity())

        }
        binding.fragmentEditProfileEmailStartEditBtn.setOnClickListener {
//            showToast(activity, getString(R.string.startEmailEdit))
            binding.fragmentEditProfileEmailStartEditBtn.visibility=View.GONE
            makeEditTextFoucasable(binding.fragmentEditProfileEmailEdit)
            editCode(binding.fragmentEditProfileEmailEdit!!, requireActivity())
        }
        binding.fragmentEditProfilePhoneNumberStartEditBtn.setOnClickListener {
            binding.fragmentEditProfilePhoneNumberStartEditBtn.visibility=View.GONE
//            showToast(activity, getString(R.string.startPhoneEdit))
            makeEditTextFoucasable(binding.fragmentEditProfilePhoneNumberEdit)
            editCode(binding.fragmentEditProfilePhoneNumberEdit!!, requireActivity())


        }
        binding.backBtn.setOnClickListener {
            super.onBack()
        }
        binding.fragmentEditProfileChangePasswordBtu.setOnClickListener {
            showBottomSheetDialog()
        }
        binding.fragmentEditProfileEditUserImageBt.setOnClickListener {
            selectProfileImage()
        }
        binding.fragmentEditProfileCircleImage.setOnClickListener {
            selectProfileImage()
        }

        binding.fragmentEditProfileEditProfileBtu.setOnClickListener {
            onValidation()
        }
    }

    private fun makeEditTextFoucasable(fragmentEdit: TextInputEditText) {
        fragmentEdit.setCursorVisible(true)
        fragmentEdit.setFocusable(true)
        fragmentEdit.setFocusableInTouchMode(true)
        fragmentEdit.setClickable(true)
    }

    private fun selectProfileImage() {
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun showBottomSheetDialog() {
//        dialogChangePassword = Dialog(requireContext(), R.style.ImageSourceDialogStyle)
        dialogChangePassword = BottomSheetDialog(requireContext())
//        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogChangePassword.setContentView(R.layout.buttom_sheet_change_password)
        dialogChangePassword.setCanceledOnTouchOutside(true)
//        val window = dialogChangePassword.window
//        window!!.setGravity(Gravity.BOTTOM)
//        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialogChangePassword.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
        val close = dialogChangePassword.findViewById<ImageView>(R.id.change_password_bottom_sheet_close_image)
        val savePasswordChangesBtn = dialogChangePassword.findViewById<Button>(R.id.fragment_edit_password_save_btu)
        savePasswordChangesBtn!!.setOnClickListener {
            onValidPasswordData()
        }
        close!!.setOnClickListener { dialogChangePassword.dismiss() }
        dialogChangePassword.show()
        KeyboardUtil(requireActivity(), dialogChangePassword.findViewById<ScrollView>(R.id.allow_nested_scroll_view)!!)
        dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_current_pass_et)!!.setOnFocusChangeListener { view, hasFocused ->
        }
        dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_new_pass_et)!!
            .setOnFocusChangeListener { view, hasFocused ->
        }
        dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_re_new_pass_til)!!.setOnFocusChangeListener { view, hasFocused ->
            if(hasFocused){
//                dialogChangePassword.padding.visibility=View.VISIBLE
            }
        }
//        dialogChangePassword.allow_nested_scroll_view.setOnTouchListener(OnTouchListener { v, event ->
//            val action = event.action
//            when (action) {
//                MotionEvent.ACTION_DOWN -> {
//                    // Disallow ScrollView to intercept touch events.
//                    dialogChangePassword.change_password_bottom_sheet_close_layout.requestDisallowInterceptTouchEvent(true)
//                    // Disable touch on transparent view
//                    false
//                }
//                MotionEvent.ACTION_UP -> {
//                    // Allow ScrollView to intercept touch events.
//                    dialogChangePassword.change_password_bottom_sheet_close_layout.requestDisallowInterceptTouchEvent(false)
//                    true
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    dialogChangePassword.change_password_bottom_sheet_close_layout.requestDisallowInterceptTouchEvent(true)
//                    false
//                }
//                else -> true
//            }
//        })

//        dialogChangePassword.fragment_new_password_current_pass_et.setOnTouchListener(OnTouchListener { v, event ->
//            v.parent.requestDisallowInterceptTouchEvent(true)
//            v.onTouchEvent(event)
//            true
//        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            imagePath=uri.toString()
            // Use Uri object instead of File to avoid storage permissions
            addNewImage(imagePath)
            Log.i(
                "Constants.TAG",
                "Uri: " + uri.toString() + "  " + uri!!.lastPathSegment + "  " + uri.path
            )
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }



    private fun addNewImage(mPath: String) {
        if (mPath != null) {
            onLoadCirImageFromUrl2(
                binding.fragmentEditProfileCircleImage, mPath,
                context
            )
        }
    }

    private fun onValidation() {
        val editTexts: List<EditText> = java.util.ArrayList()
        val spinners: List<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(binding.fragmentEditProfileFirstNameTil!!)
        textInputLayouts.add(binding.fragmentEditProfileEmailTil!!)
        textInputLayouts.add(binding.fragmentEditProfilePasswordTil!!)
        textInputLayouts.add(binding.fragmentEditProfilePhoneNumberTil!!)
        password = binding.fragmentEditProfilePasswordEdit?.getText().toString()
        phone = binding.fragmentEditProfilePhoneNumberEdit?.getText().toString().trim()
        email = binding.fragmentEditProfileEmailEdit?.getText().toString()
        name = binding.fragmentEditProfileFirstNameEdit?.getText().toString().trim()
       cleanError(textInputLayouts)
        if (password!!.trim().length == 0 || phone!!.trim().length == 0 || email!!.trim().length == 0|| name!!.trim().length == 0) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        Validation.validationLength(
            binding.fragmentEditProfileFirstNameTil,
            getString(R.string.invalid_full_name),
            2
        )
        Validation.validationEmail(requireActivity(), binding.fragmentEditProfileEmailTil)
        Validation.validationPassword(
            binding.fragmentEditProfilePasswordTil,
            7,
            getString(R.string.invalid_password)
        )
        Validation.validationLengthClearPhoneError(
            binding.fragmentEditProfilePhoneNumberTil,
            getString(R.string.invalid_phone_not_correct),
            2
        )

        if (!Validation.validationLength(
                binding.fragmentEditProfileFirstNameTil,
                getString(R.string.invalid_full_name),
                2
            )
        ) {
            editCode(binding.fragmentEditProfileFirstNameEdit!!, requireActivity())
            return
        }

        if (!Validation.validationEmail(requireActivity(), binding.fragmentEditProfileEmailTil)) {
            editCode(binding.fragmentEditProfileEmailEdit!!, requireActivity())
            return
        }
        if (!Validation.validationPassword(
                binding.fragmentEditProfilePasswordTil,
                7,
                getString(R.string.invalid_password)
            )
        ) {
            editCode(binding.fragmentEditProfilePasswordEdit!!, requireActivity())
            return
        }
        if (!Validation.validationLengthClearPhoneError(
                binding.fragmentEditProfilePhoneNumberTil,
                getString(R.string.invalid_phone_not_correct),
                2
            )
        )
        {
            editCode(binding.fragmentEditProfilePhoneNumberEdit!!, requireActivity())
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

    private fun onValidPasswordData() {
//        try {
            val edittextLayoutList: MutableList<TextInputLayout> = ArrayList()
            edittextLayoutList.add(dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_current_pass_til)!!)
            edittextLayoutList.add(dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_new_pass_til)!!)
            edittextLayoutList.add(dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_re_new_pass_til)!!)
            oldPassword =
                dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_current_pass_et)!!.getText().toString()
                    .trim()
            newPassword =
                dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_new_pass_et)!!.getText().toString().trim()
            renewPassword =
                dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_re_new_pass_et)!!.getText().toString()
                    .trim()
            cleanError(edittextLayoutList)
        Validation.validationPassword(
            dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_current_pass_til)!!,
            7,
            getString(R.string.invalid_password)
        )
        Validation.validationPassword(
            dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_new_pass_til)!!,
            7,
            getString(R.string.invalid_password)
        )
        Validation.validationPassword(
            dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_re_new_pass_til)!!,
            7,
            getString(R.string.invalid_password)
        )
        Validation.validationConfirmPassword(
            requireActivity(),
            dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_new_pass_til)!!,
            dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_re_new_pass_til)!!
        )
        if (oldPassword!!.length == 0 || newPassword!!.length == 0 || renewPassword!!.length == 0) {
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
            if (oldPassword != currentPassword) {
                dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_current_pass_til)!!.error = getString(R.string.invalid_old_password)
                editCode(dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_current_pass_et)!!, requireActivity())
            }
            if (!Validation.validationPassword(
                    dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_current_pass_til)!!,
                    7,
                    getString(R.string.invalid_password)
                )
            ) {
                editCode(dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_current_pass_et)!!, requireActivity())
                return
            }
            if (!Validation.validationPassword(
                    dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_new_pass_til)!!,
                    7,
                    getString(R.string.invalid_password)
                )
            ) {
                editCode(dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_new_pass_et)!!, requireActivity())
                return
            }

            if (!Validation.validationConfirmPassword(
                    requireActivity(),
                    dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_new_pass_til)!!,
                    dialogChangePassword.findViewById<TextInputLayout>(R.id.fragment_new_password_re_new_pass_til)!!
                )
            ) {
                editCode(dialogChangePassword.findViewById<TextInputEditText>(R.id.fragment_new_password_re_new_pass_et)!!, requireActivity())
                return
            }

            changePasswordOnCall()
//        }catch (e : Exception){}
    }

    private fun changePasswordOnCall() {
        var resetPasswordCall: Call<GetGeneralResponse?>? = null
        resetPasswordCall = ApiClient.apiClient?.userResetPassword(newPassword)
        viewModelUser!!.setGeneralAndLogout(generalDataSendedModel!!,
            resetPasswordCall!!
        )
        binding.fragmentEditProfilePasswordEdit?.setText(newPassword)
        isChangePassword=true
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
        if(isChangePassword) {
            onValidPasswordData()
        }else{
            onValidation()
        }
    }
}