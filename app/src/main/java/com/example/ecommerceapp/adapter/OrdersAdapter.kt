package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class OrdersAdapter(
    var ordersList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrdersViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_order, null, false)
        return OrdersViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: OrdersViewHolder, position: Int) {
        holder.cardParent!!.setOnClickListener { navController.navigate(R.id.orderInfoFragment) }
    }

    private fun setData(holder: OrdersViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class OrdersViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var orderCardOrderStateTv: TextView? = view.findViewById(R.id.order_card_order_state_tv)
        var orderCardNumberOrderTv: TextView? = view.findViewById(R.id.order_card_number_order_tv)
        var orderCardNameProductTv: TextView? = view.findViewById(R.id.order_card_name_product_tv)
        var cardParent: CardView? = view.findViewById(R.id.card_parent)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}