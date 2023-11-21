package com.example.ecommerceapp.adapter

import android.content.ContentValues
import android.content.ContentValues.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.MoreSellerAdapter.MoreSellerViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MoreSellerAdapter(
    var categoryNmaeList: MutableList<AllProductForRom>,
    var generalDataSendedModel: GeneralDataSendedModel,
    root: View
) :
    RecyclerView.Adapter<MoreSellerViewHolder>() {
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var bundle: Bundle?=null
    private val root: View
    private val activity: BaseActivity?
    private val context: Context
    private var dataBase: DataBaseKotlin? = null
    private lateinit var allProductForRom: AllProductForRom
    private var itemId: Int = 0
    private var items: List<AllProductForRom?>?= ArrayList<AllProductForRom>()
    var navController: NavController? = null
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreSellerViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_all_more_seller_item, null, false)
        return MoreSellerViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MoreSellerViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: MoreSellerViewHolder, position: Int) {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)

        holder.advertisementItemCard!!.setOnClickListener {
            try {
                findNavController(root).navigate(R.id.productDetailsFragment,bundle)
            } catch (e: Exception) {
            }
        }
        holder.cardItemLike!!.setOnClickListener {
            if (holder.cardMoreSellerOutLineHeart!!.visibility == View.VISIBLE) {
                holder.cardMoreSellerFillHeart!!.visibility = View.VISIBLE
                holder.cardMoreSellerOutLineHeart!!.visibility = View.GONE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love_red))
            } else {
                holder.cardMoreSellerFillHeart!!.visibility = View.GONE
                holder.cardMoreSellerOutLineHeart!!.visibility = View.VISIBLE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love))
            }
        }

        holder.cardMoreSellerAddToBinBtu!!.setOnClickListener {
            holder.cardMoreSellerAddToBinBtu!!.visibility = View.GONE
            holder.cardAllMoreSellerItemAddToBinLayout!!.visibility = View.VISIBLE
            holder.productItemCount=1
            categoryNmaeList.get(position).quantity=holder.productItemCount.toString()
            holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount
            Executors.newSingleThreadExecutor()
                .execute {
                    dataBase!!.addNewOrderItemDao()!!.insert(categoryNmaeList.get(position))
                    items= ArrayList<AllProductForRom>()
                    itemId=dataBase!!.addNewOrderItemDao()!!.loadLast().itemId
//                    Log.i(ContentValues.TAG+""+items?.size, itemId.toString())
//                    items = dataBase!!.addNewOrderItemDao()!!.allItems
//                    itemId= items?.get((items!!.size-1))!!.itemId
//                    Log.i(ContentValues.TAG+""+items?.size, itemId.toString())
                }
        }
        holder.cardAllMoreSellerItemIncreaseNumberProduct?.setOnClickListener {
            holder.productItemCount++
            categoryNmaeList.get(position).quantity=holder.productItemCount.toString()
            GlobalScope.launch { Dispatchers.IO
                Dispatchers.IO  // equal newSingleThreadExecutor
                dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(), itemId)
                try {
                    Log.i(
                        TAG + "" + items?.size,
                        dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId).quantity
                    )
                    Log.i(ContentValues.TAG + "" + items?.size, itemId.toString())
                }catch (e:Exception){}
            }
            holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount

        }
        holder.cardAllMoreSellerItemDecreaseNumberProduct?.setOnClickListener {
            if(holder.productItemCount>0) {
                if(holder.productItemCount==1) {
                    holder.cardAllMoreSellerItemAddToBinLayout!!.visibility = View.GONE
                    holder.cardMoreSellerAddToBinBtu!!.visibility = View.VISIBLE
                    Executors.newSingleThreadExecutor()
                        .execute {

                            dataBase!!.addNewOrderItemDao()!!.deleteById(itemId)
                        }
                    holder.productItemCount--
                }else{
                    holder.productItemCount--
                    Executors.newSingleThreadExecutor()
                        .execute {
                            dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(),itemId)
                            dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId)
                        }
                }
                categoryNmaeList.get(position).quantity=holder.productItemCount.toString()
                holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount

            }
        }
    }

    private fun setData(holder: MoreSellerViewHolder, position: Int) {
        dataBase = DataBaseKotlin.getInstance(activity!!)
        var productItem = categoryNmaeList.get(position)
        if (productItem.quantity.equals("0")){
//            holder.cardMoreSellerAddToBinBtu!!.visibility = View.VISIBLE
            holder.cardAllMoreSellerItemAddToBinLayout!!.visibility = View.GONE
        }else{
            holder.cardMoreSellerAddToBinBtu!!.visibility = View.GONE
            holder.cardAllMoreSellerItemAddToBinLayout!!.visibility = View.VISIBLE
            holder.productItemCount=productItem.quantity.toInt()
            holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount
        }
        Executors.newSingleThreadExecutor()
            .execute {
                try {
                    var name = dataBase!!.addNewOrderItemDao()!!.loadSingleByName("ali").productName
                    Log.i(ContentValues.TAG + "" + items?.size, name)
                }catch (e:Exception){}
            }
    }
    override fun getItemCount(): Int {
        return categoryNmaeList.size
    }

    inner class MoreSellerViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var productItemCount=0
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
        var cardAllMoreSellerItemNumberOfProductTv: TextView? = view.findViewById(R.id.card_all_more_seller_item_number_of_product)
        var cardAllMoreSellerItemDecreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_all_more_seller_item_decrease_number_product)
        var cardAllMoreSellerItemAddToBinLayout: LinearLayout? = view.findViewById(R.id.card_all_more_seller_item_add_to_bin_layout)
        var cardMoreSellerAddToBinBtu: TextView? = view.findViewById(R.id.card_more_seller_add_to_bin_btu)
        var advertisementItemCard: LinearLayout? = view.findViewById(R.id.advertisement_item_card)

        init {
//            ButterKnife.bind(this, view)
        }
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.root = root
    }
}