package com.example.ecommerceapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(var generalDataSendedModel: GeneralDataSendedModel,val sliderDataArrayList: MutableList<Int>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {
    // list for storing urls of images.
//    private val mSliderItems: List<Int>
//
//    // Constructor
//    init {
//        mSliderItems = sliderDataArrayList
//    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.material_page, null)
        return SliderAdapterViewHolder(inflate)
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        val sliderItem: Int = sliderDataArrayList[position]

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
            .load(sliderItem)
            .fitCenter()
            .into(viewHolder.imageViewItem)
    }

    // this method will return
    // the count of our list.
    override fun getCount(): Int {
        return sliderDataArrayList.size
    }

    inner class SliderAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        // Adapter class for initializing
        // the views of our slider view.
//        var itemView: View
        var imageViewItem: ImageView
        init {
            imageViewItem = itemView.findViewById(R.id.item_image)
//            this.itemView = itemView
        }
    }
}
