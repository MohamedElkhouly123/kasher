package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.AdsAdapter.TodayOffersViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.Post
import com.example.ecommerceapp.view.activity.BaseActivity

class AdsAdapter(var todayOffersList: List<Post>, var generalDataSendedModel: GeneralDataSendedModel) :
    RecyclerView.Adapter<TodayOffersViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController? = null
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    var test=""
    //////////////////////
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodayOffersViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_ads, null, false)
        return TodayOffersViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }



    override fun onBindViewHolder(holder: TodayOffersViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: TodayOffersViewHolder, position: Int) {}
    private fun setData(holder: TodayOffersViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 2
    }

    inner class TodayOffersViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel.context!!
    }
}