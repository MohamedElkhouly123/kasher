package com.example.flashBook.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.BasicItem
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity



class SubCategoriesVrAdapter(
    var subCategoryDataList: List<BasicItem>,
    val generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<SubCategoriesVrAdapter.BasicViewHolder>() {
    private val activity: BaseActivity?
    var img = 0

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
//        userdata = LoadUserData(activity!!)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_sub_category_vr_item_basic_rv, null, false)
        return BasicViewHolder(v)
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        setData(holder, position)
        //        showToast(activity, "yes "+groupAdmin);
        setAction(holder, position)
    }

    private fun setAction(holder: BasicViewHolder, position: Int) {
        holder.subCatItemCard!!.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
//            if (getItemViewType(position) == 0) {
//                navController.navigate(R.id.coursesFragment, bundle)
//                //                    navController.navigate(R.id.allPdfCategoryFragment, bundle);
//            }
        })
    }

    private fun setData(holder: BasicViewHolder, position: Int) {
//        if (getCourseDataResponse.get(position).getImagePath() != 0) {
//        img = getCourseDataResponseList[position].getImagePath()
//        onLoadImageFromUrl(holder.groupCourseItemTypeImage, img, context)
        //        }
//        holder.groupCourseItemTypeImage.setImageResource(getCourseDataResponse.get(position).getImagePath());
        if (getItemViewType(position) == 0) {
            holder.subCatItem_card_tv.text =activity!!.getString(R.string.all)
            holder.subCatItemCard.setBackgroundResource(R.drawable.shap_second_app_color_with_stroke)
            holder.subCatItem_card_products_num_tv.setTextColor(    ContextCompat.getColor(
                activity,
                R.color.black
            ))

        }else {
            holder.subCatItem_card_tv.text = subCategoryDataList[position].text
        }
//        showToast(activity, "yes "+"groupAdmin");
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return subCategoryDataList.size
    }

    inner class BasicViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var subCatItemCard:FrameLayout = view.findViewById(R.id.subCatItemCard_ly)
        var subCatItem_card_tv: TextView = view.findViewById(R.id.subCatItem_card_name_tv)
        var subCatItem_card_products_num_tv: TextView = view.findViewById(R.id.subCatItem_card_products_num_tv)

    }

    companion object {
        var groupAdminOrProfileOwner: Int = 0
    }
}