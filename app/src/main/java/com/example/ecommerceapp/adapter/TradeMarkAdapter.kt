package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.TradeMarkAdapter.TradeMarkViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class TradeMarkAdapter(
    var tradeMarkList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<TradeMarkViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeMarkViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_trade_mark, null, false)
        return TradeMarkViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: TradeMarkViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: TradeMarkViewHolder, position: Int) {}
    private fun setData(holder: TradeMarkViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 12
    }

    inner class TradeMarkViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}