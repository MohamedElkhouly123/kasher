package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel


class SliderAdapterExample(allStoreItemsImages: List<Int>, generalDataSendedModel: GeneralDataSendedModel) :
    SliderViewAdapter<SliderAdapterExample.SliderAdapterVH>(
        allStoreItemsImages,
        generalDataSendedModel
    ) {
    private val context: Context
    private var mSliderItems: MutableList<Int> = ArrayList()

    init {
        this.context = generalDataSendedModel?.context!!
    }

    fun renewItems(sliderItems: MutableList<Int>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: Int) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.material_page, null)
        return SliderAdapterVH(inflate)
    }

    fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
//        val sliderItem: SliderItem = mSliderItems[position]
//        viewHolder.textViewDescription.setText(sliderItem.getDescription())
//        viewHolder.textViewDescription.textSize = 16f
//        viewHolder.textViewDescription.setTextColor(Color.WHITE)
//        Glide.with(viewHolder.itemView)
//            .load(sliderItem.getImageUrl())
//            .fitCenter()
//            .into<Target<Drawable>>(viewHolder.imageViewBackground)
//        viewHolder.itemView.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
    }

    //slider view count could be dynamic size
    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }


    inner class SliderAdapterVH(itemView: View) : com.smarteist.autoimageslider.SliderViewAdapter.ViewHolder(itemView) {
//        var itemView: View
//        var imageViewBackground: ImageView
//        var imageGifContainer: ImageView
//        var textViewDescription: TextView

        init {
//            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider)
//            imageGifContainer = itemView.findViewById(R.id.iv_gif_container)
//            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider)
//            this.itemView = itemView
        }
    }
}