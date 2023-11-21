package com.example.ecommerceapp.utils.Dialogs

import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.BasicCountryItem
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.flashBook.adapter.CountryVrAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowBottomSheetCountryRecycleList(var generalDataSendedModel: GeneralDataSendedModel?) {

    private var countryVrAdapter: CountryVrAdapter?=null
    private lateinit var gLayout: GridLayoutManager
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var countryItemList = ArrayList<BasicCountryItem>()

    fun showBottomSheetDialog() {
//        dialogChangePassword = Dialog(requireContext(), R.style.ImageSourceDialogStyle)
        bottomSheetDialog = BottomSheetDialog(generalDataSendedModel?.activity!!)
//        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.buttom_sheet_recycle_list)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        val window = bottomSheetDialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val close : ImageView? = bottomSheetDialog.findViewById(R.id.buttom_sheet_recycle_list_close_image)
        val buttom_sheet_recycle_list_rv: RecyclerView? = bottomSheetDialog.findViewById(R.id.buttom_sheet_recycle_list_rv)
        val buttom_sheet_recycle_list_title_tv: TextView? = bottomSheetDialog.findViewById(R.id.buttom_sheet_recycle_list_title_tv)
        buttom_sheet_recycle_list_title_tv!!.setText(generalDataSendedModel!!.activity!!.getString(R.string.select_country))
        showRV(buttom_sheet_recycle_list_rv!!)
//        savePasswordChangesBtn.setOnClickListener {
//            onValidPasswordData()
//        }
        close!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun showRV(buttom_sheet_recycle_list_rv: RecyclerView) {
        countryItemList.clear()
        countryItemList.add(BasicCountryItem(R.drawable.saudi_arabia_1,"السعودية","+966",7))
        countryItemList.add(BasicCountryItem(R.drawable.flag_egypt,"مصر","+20",10))
        countryItemList.add(BasicCountryItem(R.drawable.emarat,"الامارات","+966",7))
//        countryItemList.add(BasicCountryItem(R.drawable.saudi_arabia_1,"السعودية","+966",7))
//        countryItemList.add(BasicCountryItem(R.drawable.flag_egypt,"مصر","+20",10))
//        countryItemList.add(BasicCountryItem(R.drawable.emarat,"الامارات","+966",7))

        gLayout = GridLayoutManager(generalDataSendedModel?.activity, 1)

        buttom_sheet_recycle_list_rv?.layoutManager = gLayout
        countryVrAdapter = CountryVrAdapter(
            countryItemList,generalDataSendedModel!!
        )
        buttom_sheet_recycle_list_rv?.adapter = countryVrAdapter
    }

    fun dismiss() {
        bottomSheetDialog.dismiss()
    }

}