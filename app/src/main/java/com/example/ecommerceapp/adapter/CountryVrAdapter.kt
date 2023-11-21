package com.example.flashBook.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.BasicCountryItem
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import de.hdodenhof.circleimageview.CircleImageView


class CountryVrAdapter(
    var countryDataList: List<BasicCountryItem>,
    val generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<CountryVrAdapter.BasicViewHolder>() {
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
            .inflate(R.layout.card_country_vr_item_basic_rv, null, false)
        return BasicViewHolder(v)
    }

    override fun onBindViewHolder(holder: BasicViewHolder, position: Int) {
        setData(holder, position)
        //        showToast(activity, "yes "+groupAdmin);
        setAction(holder, position)
    }

    private fun setAction(holder: BasicViewHolder, position: Int) {
        holder.card_country_vr_item_basic_rv_ly!!.setOnClickListener(View.OnClickListener {
            generalDataSendedModel.type="Change Country"
            generalDataSendedModel.countryItem= countryDataList[position]
            generalDataSendedModel.makeChangeInFragment!!.doChanges(generalDataSendedModel)
        })

    }

    private fun setData(holder: BasicViewHolder, position: Int) {
//        if (getCourseDataResponse.get(position).getImagePath() != 0) {
//        img = getCourseDataResponseList[position].getImagePath()
//        onLoadImageFromUrl(holder.groupCourseItemTypeImage, img, context)
        //        }
//        holder.groupCourseItemTypeImage.setImageResource(getCourseDataResponse.get(position).getImagePath());
        if (getItemViewType(position) == itemCount-1) {
//            holder.card_country_vr_item_basic_rv_name_tv.text =activity!!.getString(R.string.all)
//            holder.card_country_vr_item_basic_rv_ly.setBackgroundResource(R.drawable.shap_second_app_color_with_stroke)
//            holder.card_country_vr_item_basic_rv_code_tv.setTextColor(    ContextCompat.getColor(
//                activity,
//                R.color.black
//            ))
            holder.card_country_vr_item_basic_rv_line_img.visibility=View.GONE
        }else {
        }
        holder.card_country_vr_item_basic_rv_name_tv.text = countryDataList[position].countryName
        holder.card_country_vr_item_basic_rv_code_tv.text = countryDataList[position].countryCodeWithPlus
        holder.card_country_vr_item_basic_rv_circle.setImageResource(countryDataList[position].countryImge)

//        showToast(activity, "yes "+"groupAdmin");
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return countryDataList.size
    }

    inner class BasicViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var card_country_vr_item_basic_rv_ly:FrameLayout = view.findViewById(R.id.card_country_vr_item_basic_rv_ly)
        var card_country_vr_item_basic_rv_name_tv: TextView = view.findViewById(R.id.card_country_vr_item_basic_rv_name_tv)
        var card_country_vr_item_basic_rv_code_tv: TextView = view.findViewById(R.id.card_country_vr_item_basic_rv_code_tv)
        var card_country_vr_item_basic_rv_circle: CircleImageView = view.findViewById(R.id.card_country_vr_item_basic_rv_circle)
        var card_country_vr_item_basic_rv_line_img: ImageView = view.findViewById(R.id.card_country_vr_item_basic_rv_line_img)

    }

    companion object {
        var groupAdminOrProfileOwner: Int = 0
    }
}