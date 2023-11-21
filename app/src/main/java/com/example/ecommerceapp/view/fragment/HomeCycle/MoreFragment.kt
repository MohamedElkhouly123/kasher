package com.example.ecommerceapp.view.fragment.HomeCycle

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveLanguage
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.databinding.FragmentMoreBinding
import com.example.ecommerceapp.utils.Dialogs.LogoutDialog
import com.example.ecommerceapp.utils.HelperMethod.DatePickerFragment.Companion.setLocale
import com.example.ecommerceapp.utils.interfaces.MakeChangeInFragment
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.BaseActivity.Companion.languageToLoad
import com.example.ecommerceapp.view.activity.UserCycleActivity
import com.example.ecommerceapp.view.fragment.BaseFragment
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView


class MoreFragment : BaseFragment(), TryAgainOncall, MakeChangeInFragment {
    private lateinit var binding: FragmentMoreBinding
    private var counter: CountDownTimer? = null
    private var root: View? = null
    private var generalDataSendedModel: GeneralDataSendedModel?=null
    private var englishLayout: FrameLayout?=null
    private var arabicLayout: FrameLayout?=null
    private var englishRadioBtu: RadioButton?=null
    private var arabicRadioBtu: RadioButton?=null


    var navController: NavController? = null
    private var dataBase: DataBaseKotlin? = null
    var binList = ArrayList<AllProductForRom>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        root=binding.root
        return root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActivity()
        initData(root!!)
        counter = object : CountDownTimer(115, 50) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                setUpActivity()
                homeCycleActivity?.showenav()
            }
        }.start()
    }

    private fun initData(root: View) {
        dataBase = DataBaseKotlin.getInstance(requireContext())
        navController = Navigation.findNavController(root)
        homeCycleActivity?.showenav()
        if(homeCycleActivity!!.userDataAuth){
            binding.fragmentMoreEditProfileBtu.visibility=View.VISIBLE
            binding.fragmentMoreUserNameTv.setText("محمد سعيد")
        }else{
            binding.fragmentMoreLogOutTv.setText(getString(R.string.login))
        }
        onClick(root)

    }

    override fun onStart() {
        super.onStart()
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun onClick(root: View) {
        generalDataSendedModel= GeneralDataSendedModel()
        generalDataSendedModel!!.activity=requireActivity()
        generalDataSendedModel!!.tryAgainCall=this
        generalDataSendedModel!!.navController=navController
        generalDataSendedModel!!.makeChangeInFragment=this
        generalDataSendedModel?.context=requireContext()

        binList= dataBase!!.addNewOrderItemDao()!!.allItems as ArrayList<AllProductForRom>

//        homeCycleActivity?.generalDataSendedModelPub?.appLanguage
        binding.fragmentMoreBinBtu!!.setOnClickListener {
//            if (binList.size>0) {
                navController!!.navigate(R.id.binFragment)
//            }else{
//                HelperMethod.showCookieMsg(
//                    getString(R.string.warning),
//                    getString(R.string.please_must_add_any_product_to_your_bin_first),
//                    getActivity(),
//                    R.color.red2,
//                    CookieBar.TOP
//                )
//            }
        }
        binding.fragmentMoreEditProfileBtu!!.setOnClickListener {
            if(homeCycleActivity!!.userDataAuth){
                navController?.navigate(R.id.editProfileFragment)
            }else {
                generalDataSendedModel!!.startLogin=true
                homeCycleActivity!!.checkUserAuthAndInternetFun(generalDataSendedModel!!)
            }
        }
        binding.fragmentMoreOrdersBtu!!.setOnClickListener {
            if(homeCycleActivity!!.userDataAuth){
                navController?.navigate(R.id.ordersFragment)
            }else {
                generalDataSendedModel!!.startLogin=true
                homeCycleActivity!!.checkUserAuthAndInternetFun(generalDataSendedModel!!)
            }
        }
        binding.fragmentMoreAddressBtu!!.setOnClickListener { navController?.navigate(R.id.addressFragment) }
        binding.fragmentMoreChangeLanguageBtu!!.setOnClickListener { showBottomSheetDialog()}
        binding.fragmentMoreTermsConditionsBtu!!.setOnClickListener {navController!!.navigate(R.id.termsAndCondationFragment) }
        binding.fragmentMorePolicyPrivacyBtu!!.setOnClickListener { navController?.navigate(R.id.privacyFragment) }
        binding.fragmentMorePolicyContactUsBtu!!.setOnClickListener { navController?.navigate(R.id.contactUsFragment) }
        binding.fragmentMoreLogOutBtu!!.setOnClickListener {
            val dialog2 = LogoutDialog()
            dialog2.showDialog(generalDataSendedModel!!)
//            showDialoge(requireActivity(),getString(R.string.warning),
//                getString(R.string.Areyousuretoquite),
//                getString(R.string.log_out),
//                getString(R.string.cancel),
//                { dialog: DialogInterface?, ok: Int -> logOutFun() }
//            ) { dialog: DialogInterface, no: Int -> dialog.dismiss() }
        }
    }

    private fun logOutFun() {
//        val languageCode: String = LoadLanguage(activity, "LANGUAGE")
//        languageToLoad = languageCode
        SharedPreferencesManger.clean(requireActivity())
//        SaveData(activity, PeopleSearch, searchData)
        SaveLanguage(requireActivity(), "LANGUAGE", languageToLoad)
//        val i = Intent(activity, UserCycleActivity::class.java)
//        requireActivity().startActivity(i)
//        // close this activity
//        requireActivity().finish()
//        onCall()
    }

    private fun showBottomSheetDialog() {
        try{
        val dialogChangeLanguage = BottomSheetDialog(requireContext())
        dialogChangeLanguage.setContentView(R.layout.change_language_buttom_sheet)
        dialogChangeLanguage.setCanceledOnTouchOutside(true)

        arabicRadioBtu =
            dialogChangeLanguage.findViewById<RadioButton>(R.id.change_language_bottom_sheet_arabic_radio_bottom)
        englishRadioBtu =
            dialogChangeLanguage.findViewById<RadioButton>(R.id.change_language_bottom_english_radio_btu)
        arabicLayout =
            dialogChangeLanguage.findViewById<FrameLayout>(R.id.change_language_bottom_sheet_arabic)
        englishLayout =
            dialogChangeLanguage.findViewById<FrameLayout>(R.id.change_language_bottom_sheet_english)
        val cancel =
            dialogChangeLanguage.findViewById<LinearLayout>(R.id.cancel_bottom_sheet_layout)
        cancel!!.setOnClickListener { dialogChangeLanguage.dismiss() }

        if(BaseActivity.languageToLoad?.length!! >0){
            if("ar".equals(BaseActivity.languageToLoad)){
                setArabicLanguage()
            }else{
                setEnglishLanguage()
            }
        }
        arabicRadioBtu?.setOnClickListener {
            setArabicLanguage()
            BaseActivity.languageToLoad = "ar"
            setLocale(
                requireActivity(),
                BaseActivity.languageToLoad,
                2,
                ""
            )
        }
        englishRadioBtu?.setOnClickListener {
            setEnglishLanguage()
            BaseActivity.languageToLoad = "en"
            setLocale(
                requireActivity(),
                BaseActivity.languageToLoad,
                2,
                ""
            )
        }
        dialogChangeLanguage.show()
        }catch (e:Exception){}
    }

    private fun setArabicLanguage() {
        arabicRadioBtu?.isChecked = true
        englishRadioBtu?.isChecked = false
        arabicLayout?.setBackgroundColor(
            requireActivity().getResources().getColor(R.color.app_second_color_sky)
        )
        englishLayout?.setBackgroundColor(requireActivity().getResources().getColor(R.color.white))    }


    override fun onBack() {
        navController?.popBackStack()
        navController?.navigate(R.id.homeFragment2)
    }


    fun setEnglishLanguage(){
        arabicRadioBtu?.isChecked = false
        englishRadioBtu?.isChecked = true
        arabicLayout?.setBackgroundColor(requireActivity().getResources().getColor(R.color.white))
        englishLayout?.setBackgroundColor(
            requireActivity().getResources().getColor(R.color.app_second_color_sky)
        )
    }

    override fun tryAgainCall(generalDataSendedModel: GeneralDataSendedModel) {
    }

    override fun doChanges(generalDataSendedModel: GeneralDataSendedModel) {

    }


}