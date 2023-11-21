package com.example.ecommerceapp.view.fragment.subPages

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
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
import com.example.ecommerceapp.databinding.FragmentCompleteBinding
import com.example.ecommerceapp.databinding.FragmentOrdersDetailsBinding
import com.example.ecommerceapp.utils.Dialogs.DialogDone
import com.example.ecommerceapp.utils.GPSTracker
import com.example.ecommerceapp.utils.HelperMethod
import com.example.ecommerceapp.utils.HelperMethod.showToast
import com.example.ecommerceapp.utils.KeyboardUtil
import com.example.ecommerceapp.utils.ToastCreator
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.validation.Validation
import com.example.ecommerceapp.view.ViewModel.ViewModelUserAndPostRequests
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AddressesForRoom
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import jp.wasabeef.blurry.Blurry

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CompleteOrderFragment : BaseFragment(), DatePickerDialog.OnDateSetListener ,TryAgainOncall,MakeChangeInFragment{
    private lateinit var navController: NavController
    private lateinit var completeFragmentCashCardLayout: LinearLayout
    private lateinit var completeFragmentCreditCardLayout: LinearLayout
    private lateinit var binding: FragmentCompleteBinding
    private lateinit var gpsTracker: GPSTracker
    private var root: View?=null
    private lateinit var viewModelUser: ViewModelUserAndPostRequests
    private var cancelImage: ImageView?=null
    private var cancelBtu: TextView?=null
    private var completePaidBtu: TextView?=null
    private var NumberCvv: EditText?=null
    private var numberCredit: EditText?=null
    private var dateExpire: EditText?=null
    private var expireDate: String?=null
    private var addressSelected: AddressesForRoom? =null


    var openCreditButtomSheet = false
    var dialogShowCreditCardDetails: BottomSheetDialog? = null
    var bundle = Bundle()
    private var dataBase: DataBaseKotlin? = null
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
        binding = FragmentCompleteBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        onClick(root!!)
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
                            requireActivity().finish()
                        }
                        //                        }
                    } else {
                    }
                } else {
                }
            })
    }
    fun onClick(root: View) {
        navController = Navigation.findNavController(root)
        homeCycleActivity?.hidenav()
        dataBase = DataBaseKotlin.getInstance(requireContext())
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.makeChangeInFragment=this
        generalDataSendedModel?.context=requireContext()
        generalDataSendedModel?.homeCycleActivity=homeCycleActivity
        if (homeCycleActivity?.addressSelectedItem?.selected!!) {
            addressSelected = homeCycleActivity?.addressSelectedItem
        } else {
            addressSelected = dataBase!!.addNewOrderItemDao()!!.loadSingleAddressSelected(true)
        }
        if(addressSelected?.address!=null&&addressSelected?.address?.trim()?.length!! >0){
            setDeleveryToData()
        }else{
            gpsTracker = GPSTracker(generalDataSendedModel)
            gpsTracker.startTrace()
            homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)
        }
        completeFragmentCreditCardLayout=binding.completeFragmentCreditCardLayout
        completeFragmentCashCardLayout=binding.completeFragmentCashCardLayout
        val bundle = Bundle()
//        Blurry.with(context)
//            .radius(10)
//            .sampling(8)
////            .color(Color.argb(66, 255, 255, 0))
//            .async()
//            .onto(root!!.fragment_ask_one_orders_add_national_image_card)
//        val blurEffect = RenderEffect.createBlurEffect(1.0F, 1.0F, Shader.TileMode.MIRROR)
//        root.fragment_ask_one_orders_add_national_image_card.setRenderEffect(blurEffect)
        binding.backBtn.setOnClickListener{super.onBack()}
        binding.completeFragmentCreditCardLayout.setOnClickListener{
//                completeFragmentCreditCardLayout!!.setBackgroundColor(
//                    requireActivity().getResources().getColor(R.color.app_second_color_sky)
//                )
//                completeFragmentCashCardLayout!!.setBackgroundColor(
//                    requireActivity().getResources().getColor(R.color.white)
//                )
            completeFragmentCreditCardLayout!!.setBackgroundResource(R.drawable.shap_second_app_color_with_stroke)
            completeFragmentCashCardLayout!!.setBackgroundResource(R.drawable.shap_app_color_with_stroke)
                openCreditButtomSheet = true
            }

        binding.completeFragmentCashCardLayout.setOnClickListener{
                completeFragmentCreditCardLayout!!.setBackgroundResource(R.drawable.shap_app_color_with_stroke)
                completeFragmentCashCardLayout!!.setBackgroundResource(R.drawable.shap_second_app_color_with_stroke)
                openCreditButtomSheet = false
            }
        binding.fragmentCompleteOrderGoToMapBtu.setOnClickListener {
//            bundle.putString("type", "home")
//            Navigation.findNavController(root!!)?.navigate(R.id.addressFragment,bundle)
            Executors.newSingleThreadExecutor()
                .execute {
                    val adressesList= mutableListOf<AddressesForRoom>()
                    adressesList.clear()
                    adressesList.addAll(dataBase!!.addNewOrderItemDao()!!.allAddressesItems as ArrayList<AddressesForRoom>)
                    homeCycleActivity?.addressList!!.clear()
                    homeCycleActivity?.addressList!!.addAll(adressesList)
                    try {
                        Log.i(
                            ContentValues.TAG + "" + adressesList?.size,
                            adressesList[0].address.toString()
                        )
                    } catch (e: Exception) {
                    }
                    runBlocking (Dispatchers.Main) {
                        if (adressesList.size>0) {
//                            bundle.putSerializable("adressesList", adressesList as Serializable?)
                            bundle.putString("type", "home")
                            var generalBundelDataToSend = GeneralBundelDataToSend()
                            generalBundelDataToSend.from="haveList"

                            bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
                            Navigation.findNavController(root!!)
                                ?.navigate(R.id.addressFragment, bundle)
                        }else{
                            Navigation.findNavController(root!!)
                                ?.navigate(R.id.mapFragment, bundle)
                        }
                    }
                }
        }
        binding.fragmentCompleteOrderBtu.setOnClickListener {
            if(! homeCycleActivity!!.userDataAuth){
                generalDataSendedModel!!.startLogin=true
                homeCycleActivity!!.checkUserAuthAndInternetFun(generalDataSendedModel!!)
            }else{
                if (openCreditButtomSheet) {
                    initDialogeShowCoponeDetails()
                } else {
                    val dialog = DialogDone()
                    dialog.showDialog(generalDataSendedModel!!, getString(R.string.success_send))
//                bundle.putString("type", "home")
                    Navigation.findNavController(root!!)?.navigate(R.id.rateFragment, bundle)
                }

            }
        }

    }

    private fun initDialogeShowCoponeDetails() {
        dialogShowCreditCardDetails = BottomSheetDialog(requireContext())
        dialogShowCreditCardDetails!!.setContentView(R.layout.credit_card_buttom_sheet_details)
        dialogShowCreditCardDetails!!.setCanceledOnTouchOutside(true)
//        val window = dialogShowCreditCardDetails!!.window
//        window!!.setGravity(Gravity.BOTTOM)
//        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        dialogShowCreditCardDetails!!.window!!
//            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        numberCredit =
            dialogShowCreditCardDetails!!.findViewById<EditText>(R.id.credit_card_buttom_sheet_number_credit_edit)
        dateExpire =
            dialogShowCreditCardDetails!!.findViewById<EditText>(R.id.credit_card_buttom_sheet_date_expire_credit_edit)
        NumberCvv =
            dialogShowCreditCardDetails!!.findViewById<EditText>(R.id.credit_card_buttom_sheet_number_cvv_credit_edit)
        completePaidBtu =
            dialogShowCreditCardDetails!!.findViewById<TextView>(R.id.credit_card_buttom_sheet_complete_paid_btu)
        cancelBtu =
            dialogShowCreditCardDetails!!.findViewById<TextView>(R.id.credit_card_buttom_sheet_cancel_paid_btu)
        cancelImage =
            dialogShowCreditCardDetails!!.findViewById<ImageView>(R.id.credit_card_buttom_sheet_cancel_image)
        cancelImage?.setOnClickListener { dialogShowCreditCardDetails!!.dismiss() }
        completePaidBtu?.setOnClickListener {
//            dialogShowCreditCardDetails!!.dismiss()
//            val dialog = DialogDone()
//            dialog.showDialog(getActivity(), getString(R.string.success_send))
            onValidation()
        }
        dateExpire?.setOnClickListener {
          showDate()
        }
        cancelBtu?.setOnClickListener { dialogShowCreditCardDetails!!.dismiss() }


//        CardView closeDialoge = dialogShowCreditCardDetails.findViewById(R.id.copone_dialog_close_dialog);
//        TextView copyCode=dialogShowCreditCardDetails.findViewById(R.id.dialog_copone_details_copy_text);
        dialogShowCreditCardDetails!!.show()
        KeyboardUtil(requireActivity(), dialogShowCreditCardDetails!!.findViewById<ScrollView>(R.id.credit_card_buttom_sheet_nested_scroll)!!)

    }
    private fun onValidation() {
        val editTexts: MutableList<EditText> = java.util.ArrayList()
        val spinners: List<Spinner> = java.util.ArrayList()
        val textInputLayouts: MutableList<TextInputLayout> = java.util.ArrayList()
        textInputLayouts.add(dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_credit_til)!!)
        textInputLayouts.add(dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_cvv_credit_til)!!)
        textInputLayouts.add(dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_date_expire_credit_til)!!)

        var cridetNum = numberCredit?.text.toString().trim()
        var cvvNum = NumberCvv?.text.toString().trim()

        Validation.cleanError(textInputLayouts)
        if(cridetNum!!.trim().length==0||cvvNum!!.trim().length==0||expireDate==null||expireDate?.trim()?.length==0){
            ToastCreator.onCreateErrorToastFull(requireActivity()!!, getString(R.string.empty))
        }
        Validation.validationLength(
            dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_credit_til)!!,
            getString(R.string.invalid_cridet_num),
            2
        )
        Validation.validationLength(
            dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_cvv_credit_til)!!,
            getString(R.string.invalid_cvv),
            2
        )
        Validation.validationLength(
            dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_date_expire_credit_til)!!,
            getString(R.string.invalid_expired_date),
            2
        )
        if (!Validation.validationLength(
                dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_credit_til)!!,
                getString(R.string.invalid_cridet_num),
                2
            )
        ) {
            HelperMethod.editCode(numberCredit!!, requireActivity())
            return
        }
        if (!Validation.validationLength(
                dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_number_cvv_credit_til)!!,
                getString(R.string.invalid_cvv),
                2
            )
        ) {
            HelperMethod.editCode(NumberCvv!!, requireActivity())
            return
        }
        if (!Validation.validationLength(
                dialogShowCreditCardDetails!!.findViewById<TextInputLayout>(R.id.credit_card_buttom_sheet_date_expire_credit_til)!!,
                getString(R.string.invalid_expired_date),
                2
            )
        ) {
            HelperMethod.editCode(dateExpire!!, requireActivity())
            return
        }

        completeOrderCall()

    }

    private fun completeOrderCall() {

        var clientCall: Call<GetRegisterResponse?>? =null
        clientCall = ApiClient.apiClient?.login("phone", "rateMessage", BaseActivity.languageToLoad)!!
        viewModelUser!!.setLoginAndRegister(
            generalDataSendedModel!!,clientCall
        )
    }


    private fun showDate() {
        val min_date_c = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.newInstance(
            this,
            min_date_c[Calendar.YEAR],  // Initial year selection
            min_date_c[Calendar.MONTH],  // Initial month selection
            min_date_c[Calendar.DAY_OF_MONTH] // Inital day selection
        )
        val days = arrayOfNulls<Calendar>(13)
        for (i in -6..6) {
            val day = Calendar.getInstance()
            day.add(Calendar.DAY_OF_MONTH, i * 2)
            days[i + 6] = day
        }
        datePickerDialog.minDate = min_date_c
        // Setting Max Date to next 2 years
        val max_date_c = Calendar.getInstance()
        max_date_c[Calendar.YEAR] = min_date_c[Calendar.YEAR] + 4
        datePickerDialog.maxDate = max_date_c
        //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
        var loopdate = min_date_c
        while (min_date_c.before(max_date_c)) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
            min_date_c.add(Calendar.DATE, 1)
            loopdate = min_date_c
        }
        datePickerDialog.show(requireFragmentManager(), "Datepickerdialog")

    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val symbols = DecimalFormatSymbols(Locale.US)
        val mFormat = DecimalFormat("00", symbols)
        val date =
            year.toString() + "-" + mFormat.format(java.lang.Double.valueOf((monthOfYear + 1).toDouble())) + "-" + mFormat.format(
                java.lang.Double.valueOf(dayOfMonth.toDouble())
            )
        dateExpire?.setText(date)
        expireDate = date
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {

    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {
        try {
            if (homeCycleActivity?.addressSelectedItem?.selected!!) {
                try {
                    generalDataSendedModel?.addressSelectedItem!!.selected =
                        homeCycleActivity?.addressSelectedItem?.selected!!
                    this.generalDataSendedModel = GPSTracker.getAddressWithDetails(
                        generalDataSendedModel!!
                    )
//                  showToast(getActivity(), "success "+generalDataSendedModel?.addressSelectedItem!!.address)
                    homeCycleActivity?.addressSelectedItem =
                        generalDataSendedModel?.addressSelectedItem!!
                    homeCycleActivity?.addressSelectedItem?.title =
                        getString(R.string.current_location)
                    val currentAddress = homeCycleActivity?.addressSelectedItem?.address
                    if (currentAddress == null || currentAddress?.trim()?.length == 0) {
//                        homeCycleActivity?.addressSelectedItem = currentAddress2!!
                    } else {
                        SharedPreferencesManger.SaveData(
                            requireActivity()!!,
                            SharedPreferencesManger.ADRESS_DATA,
                            homeCycleActivity?.addressSelectedItem
                        )
//                            showToast(getActivity(), "success4 ")
                    }
                } catch (e: Exception) {
                }
                addressSelected = homeCycleActivity?.addressSelectedItem
                setDeleveryToData() } else {
                addressSelected = addressSelected
                setDeleveryToData()
           }
        }catch (e:Exception){}
    }

    private fun setDeleveryToData() {
        if (homeCycleActivity?.addressSelectedItem?.selected!!) {
            addressSelected?.title =
                getString(R.string.current_location)
            homeCycleActivity?.addressSelectedItem?.title = getString(R.string.current_location)
        }
        binding.fragmentCompleteOrderAddressTv.setTextColor(getResources().getColor(R.color.black_595959))
        binding.fragmentCompleteOrderAddressTv.setText(addressSelected?.title+" ( "+ addressSelected?.street+" ، "+addressSelected?.area+" )")

    }
}