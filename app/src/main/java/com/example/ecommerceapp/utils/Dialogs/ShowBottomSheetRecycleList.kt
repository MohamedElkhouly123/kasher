package com.example.ecommerceapp.utils.Dialogs

import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.BasicItem
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.flashBook.adapter.SubCategoriesVrAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowBottomSheetRecycleList(var generalDataSendedModel: GeneralDataSendedModel?) {

    private var subCategoriesVrAdapter: SubCategoriesVrAdapter?=null
    private lateinit var gLayout: GridLayoutManager
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var subCategoryList = ArrayList<BasicItem>()

    fun showBottomSheetDialog() {
//        dialogChangePassword = Dialog(requireContext(), R.style.ImageSourceDialogStyle)
        bottomSheetDialog = BottomSheetDialog(generalDataSendedModel?.activity!!)
//        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.buttom_sheet_recycle_list)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
//        val window = dialogChangePassword.window
//        window!!.setGravity(Gravity.BOTTOM)
//        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialogChangePassword.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
        val close : ImageView? = bottomSheetDialog.findViewById(R.id.buttom_sheet_recycle_list_close_image)
        val buttom_sheet_recycle_list_rv: RecyclerView? = bottomSheetDialog.findViewById(R.id.buttom_sheet_recycle_list_rv)
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
        subCategoryList.clear()
        subCategoryList.add(BasicItem(0,""))
        subCategoryList.add(BasicItem(0,"nokia"))
        subCategoryList.add(BasicItem(0,"hawawy"))
        subCategoryList.add(BasicItem(0,"opo"))
        subCategoryList.add(BasicItem(0,"nokia"))
        subCategoryList.add(BasicItem(0,"hawawy"))
        subCategoryList.add(BasicItem(0,"opo"))
        gLayout = GridLayoutManager(generalDataSendedModel?.activity, 1)

        buttom_sheet_recycle_list_rv?.layoutManager = gLayout
        subCategoriesVrAdapter = SubCategoriesVrAdapter(
            subCategoryList,generalDataSendedModel!!
        )
        buttom_sheet_recycle_list_rv?.adapter = subCategoriesVrAdapter
    }

}