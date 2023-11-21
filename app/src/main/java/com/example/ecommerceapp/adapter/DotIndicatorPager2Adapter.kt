package com.example.ecommerceapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel


class DotIndicatorPager2Adapter(
    private var generalDataSendedModel: GeneralDataSendedModel,
    private var viewPager: ViewPager2,
    private var imageList: MutableList<Int>
) : RecyclerView.Adapter<DotIndicatorPager2Adapter.MyViewHolder>() {

    data class Card(val id: Int)

//    val items = mutableListOf<Int>().apply {
//        repeat(10) { add(imageList(p)) }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return object : MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.material_page, parent, false)
        ) {

        }
    }


    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Empty
//        if (position == imageList.size-1){
//
//            viewPager.post(runnable)
//        }
    }

//    private val runnable = Runnable {
//        imageList.clear()
//        imageList.addAll(imageList)
//        notifyDataSetChanged()
//    }

    open class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        var tvName: TextView

        init {
//            tvName = itemView.findViewById(com.example.ecommerceapp.R.id.tvName)
        }
    }
}