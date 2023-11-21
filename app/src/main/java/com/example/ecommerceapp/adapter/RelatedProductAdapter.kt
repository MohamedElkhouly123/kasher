package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.RelatedProductAdapter.RelatedProductViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class RelatedProductAdapter(
    var relatedProductList: List<Post>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<RelatedProductViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RelatedProductViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_all_more_seller_item, null, false)
        return RelatedProductViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RelatedProductViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: RelatedProductViewHolder, position: Int) {
        holder.advertisementItemCard!!.setOnClickListener { }
        holder.cardItemLike!!.setOnClickListener {
            if (holder.cardMoreSellerOutLineHeart!!.visibility == View.VISIBLE) {
                holder.cardMoreSellerOutLineHeart!!.visibility = View.GONE
                holder.cardMoreSellerFillHeart!!.visibility = View.VISIBLE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love_red))
            } else {
                holder.cardMoreSellerOutLineHeart!!.visibility = View.VISIBLE
                holder.cardMoreSellerFillHeart!!.visibility = View.GONE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love))
            }
        }
        holder.cardMoreSellerAddToBinBtu!!.setOnClickListener {
            holder.cardMoreSellerAddToBinBtu!!.visibility = View.GONE
            holder.cardAllMoreSellerItemAddToBinLayout!!.visibility = View.VISIBLE
        }
    }

    private fun setData(holder: RelatedProductViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class RelatedProductViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        var moreSellerImage: ImageView? = view.findViewById(R.id.more_seller_image)
        var cardMoreSellerOutLineHeart: ImageView? = view.findViewById(R.id.card_more_seller_out_line_heart)
        var cardMoreSellerFillHeart: ImageView? = view.findViewById(R.id.card_more_seller_fill_heart)
        var cardItemLike: FrameLayout? = view.findViewById(R.id.card_item_like)
        var itemName: TextView? = view.findViewById(R.id.item_name)
        var price: TextView? = view.findViewById(R.id.price)
        var cardMoreSellerPriceMoneyTv: TextView? = view.findViewById(R.id.card_more_seller_price_money_tv)
        var cardAdvertisementNoPriceTv: TextView? = view.findViewById(R.id.card_advertisement_no_price_tv)
        var cardAdvertisementPriceLayout: LinearLayout? = view.findViewById(R.id.card_advertisement_price_layout)
        var cardAllMoreSellerItemIncreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_all_more_seller_item_increase_number_product)
        var cardAllMoreSellerItemNumberOfProduct: TextView? = view.findViewById(R.id.card_all_more_seller_item_number_of_product)
        var cardAllMoreSellerItemDecreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_all_more_seller_item_decrease_number_product)
        var cardAllMoreSellerItemAddToBinLayout: LinearLayout? = view.findViewById(R.id.card_all_more_seller_item_add_to_bin_layout)
        var cardMoreSellerAddToBinBtu: TextView? = view.findViewById(R.id.card_more_seller_add_to_bin_btu)
        var advertisementItemCard: LinearLayout? = view.findViewById(R.id.advertisement_item_card)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}