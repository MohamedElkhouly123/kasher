package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class SubRentalPaidAdapter(
    var BinList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<SubRentalPaidAdapter.BinViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    var open = false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BinViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_sub_rental_item, null, false)
        return BinViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BinViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: BinViewHolder, position: Int) {}
    private fun setData(holder: BinViewHolder, position: Int) {
        holder.cardProductDetailsDetailsTv!!.text = BinList[position].chatText
        holder.cardProductDetailsNameTv!!.text = BinList[position].time
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class BinViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var cardProductDetailsNameTv: TextView? = view.findViewById(R.id.card_product_details_name_tv)
        var cardProductDetailsDetailsTv: TextView? = view.findViewById(R.id.card_product_details_details_tv)
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}