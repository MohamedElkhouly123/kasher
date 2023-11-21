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
import com.example.ecommerceapp.adapter.ProductOverviewAdapter.ProductOverViewViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class ProductOverviewAdapter(
    var overViewList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<ProductOverViewViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductOverViewViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_product_overview, null, false)
        return ProductOverViewViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ProductOverViewViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: ProductOverViewViewHolder, position: Int) {}
    private fun setData(holder: ProductOverViewViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class ProductOverViewViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        var cardProductOverviewNameTv: TextView? = view.findViewById(R.id.card_product_overview_name_tv)
        var cardProductOverviewDetailsTv: TextView? = view.findViewById(R.id.card_product_overview_details_tv)

        init {
//            ButterKnife.bind(this, view)
        }
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}