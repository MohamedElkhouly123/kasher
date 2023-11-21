package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class HomeCircleItemAdapter(
    var ordersList: List<Post>,
   var generalDataSendedModel: GeneralDataSendedModel,
    var root: View
) :
    RecyclerView.Adapter<HomeCircleItemAdapter.HomeCircleItemViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCircleItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_home_circle_item, null, false)
        return HomeCircleItemViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: HomeCircleItemViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: HomeCircleItemViewHolder, position: Int) {
//        holder.cardParent?.setOnClickListener { Navigation.findNavController(root!!).navigate(R.id.orderInfoFragment) }
    }

    private fun setData(holder: HomeCircleItemViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class HomeCircleItemViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
//        var orderCardOrderStateTv: TextView? = view.order_card_order_state_tv
//        var orderCardNumberOrderTv: TextView? = view.order_card_number_order_tv
//        var orderCardNameProductTv: TextView? = view.order_card_name_product_tv
//        var cardParent: CardView? = view.card_parent

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
    }
}