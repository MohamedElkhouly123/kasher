package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity

open class SliderViewAdapter<T>(
    private val allStoreItemsImages: List<Int>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    PagerAdapter() {
    var activity: BaseActivity?
    private var isMultiScr: Boolean=true
    override fun getCount(): Int {
        return 5
        //        return allStoreItemsImages.size();
//        return allStoreItemsImages.size();
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val linearLayout = LayoutInflater.from(container.context)
            .inflate(R.layout.slider_item_container, null) as LinearLayout
        val imv = linearLayout.findViewById<View>(R.id.image_slider) as ImageView
        when (position) {
            0 -> imv.setImageResource(R.drawable.devise)
            1 -> imv.setImageResource(R.drawable.devise)
        }


//        if (allStoreItemsImages.get(position).getImage() != null) {
////            showToast2( context, ""+allStoreItemsImages.get(position).getImage());
////            onLoadImageFromUrl2(imv, allStoreItemsImages.get(position).getImage(), context);
//            if(allStoreItemsImages.get(position).getImage().contains("svg")){
//                onLoadImageFromUrl3( imv, BASE_URL_IMAGES+allStoreItemsImages.get(position).getImage(), activity);
//            }else {
//                onLoadImageFromUrl2( imv,BASE_URL_IMAGES+allStoreItemsImages.get(position).getImage(), context);
//            }
        imv.setOnClickListener {
            //                    new DialogShowImage().showDialog(activity, BASE_URL_IMAGES+allStoreItemsImages.get(position).getImage(),context);
            //                    showBottomSheetDialog(activity,BASE_URL_IMAGES+allStoreItemsImages.get(position).getImage(),context);
            //                    showBottomSheetDialog2(activity,allStoreItemsImages,activity,position);
        }
        //        }
//        }
        linearLayout.id = R.id.item_id
        container.addView(linearLayout)
        //        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return linearLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as LinearLayout
        container.removeView(view)
    }

    init {
        this.activity = generalDataSendedModel?.activity as BaseActivity?
        this.isMultiScr = isMultiScr
    }
}