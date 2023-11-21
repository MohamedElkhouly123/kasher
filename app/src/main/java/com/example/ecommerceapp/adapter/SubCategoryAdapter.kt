package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.SubCategoryAdapter.SubCategoryViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class SubCategoryAdapter(
    var subCategoryList: List<AllProductForRom>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<SubCategoryViewHolder>() {
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var bundle: Bundle?=null
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    private var dataBase: DataBaseKotlin? = null
    private lateinit var allProductForRom: AllProductForRom
    private var itemId: Int = 0
    private var items: List<AllProductForRom?>?= ArrayList<AllProductForRom>()

    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_all_sub_category_item, null, false)
        return SubCategoryViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: SubCategoryViewHolder, position: Int) {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        holder.cardItemLike!!.setOnClickListener {
            if (holder.cardSubCategoryOutLineHeart!!.visibility == View.VISIBLE) {
                holder.cardSubCategoryOutLineHeart!!.visibility = View.GONE
                holder.cardMoreSellerFillHeart!!.visibility = View.VISIBLE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love_red))
            } else {
                holder.cardSubCategoryOutLineHeart!!.visibility = View.VISIBLE
                holder.cardMoreSellerFillHeart!!.visibility = View.GONE
                holder.cardItemLike!!.setBackgroundDrawable(context.resources.getDrawable(R.drawable.circle_shap_love))
            }
        }
        holder.cardAllSubCategoryItemAddToBinBtu!!.setOnClickListener {
            holder.cardAllSubCategoryItemAddToBinLayout!!.visibility = View.VISIBLE
            holder.cardAllSubCategoryItemAddToBinBtu!!.visibility = View.GONE
            holder.productItemCount=1
            holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount
            Executors.newSingleThreadExecutor()
                .execute {
                    allProductForRom = AllProductForRom(
                        "shewips",
                        "50",
                        "cat",
                        "1",
                        "ddfdf",
                        "very good place",
                        "",
                        "t",
                        "f",
                        "maincat",
                        "elmasry",
                        "1"
                    )
                    dataBase!!.addNewOrderItemDao()!!.insert(allProductForRom)
                    items= ArrayList<AllProductForRom>()
                    items = dataBase!!.addNewOrderItemDao()!!.allItems
                    itemId= items?.get((items!!.size-1))!!.itemId
                }
        }


        holder.advertisementItemCard!!.setOnClickListener {
            try {
                navController.navigate(R.id.productDetailsFragment,bundle)
            } catch (e: Exception) {
            }
        }
        holder.advertisementItemCard!!.setOnClickListener {
            try {
                navController.navigate(R.id.productDetailsFragment)
            } catch (e: Exception) {
            }
        }
        holder.cardAllMoreSellerItemIncreaseNumberProduct?.setOnClickListener {
            holder.productItemCount++
            GlobalScope.launch { Dispatchers.IO
                Dispatchers.IO  // equal newSingleThreadExecutor
                dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(), itemId)
//                Log.i(ContentValues.TAG+""+items?.size, dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId).quantity)
            }
            holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount

        }
        holder.cardAllMoreSellerItemDecreaseNumberProduct?.setOnClickListener {
            if(holder.productItemCount>0) {
                if(holder.productItemCount==1) {
                    holder.cardAllSubCategoryItemAddToBinLayout!!.visibility = View.GONE
                    holder.cardAllSubCategoryItemAddToBinBtu!!.visibility = View.VISIBLE
                    Executors.newSingleThreadExecutor()
                        .execute {
//                            Log.i(ContentValues.TAG+""+items?.size, itemId.toString())
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
                holder.cardAllMoreSellerItemNumberOfProductTv?.text=""+holder.productItemCount

            }
        }
    }

    private fun setData(holder: SubCategoryViewHolder, position: Int) {
        dataBase = DataBaseKotlin.getInstance(activity!!)


    }


    override fun getItemCount(): Int {
        return 20
    }

    inner class SubCategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var productItemCount=0
        var subCategoryImage: ImageView? = view.findViewById(R.id.sub_category_image)
        var cardSubCategoryOutLineHeart: ImageView? = view.findViewById(R.id.card_sub_category_out_line_heart)
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
        var cardAllSubCategoryItemAddToBinLayout: LinearLayout? = view.findViewById(R.id.card_all_sub_category_item_add_to_bin_layout)
        var cardAllSubCategoryItemAddToBinBtu: TextView? = view.findViewById(R.id.card_all_sub_category_item_add_to_bin_btu)
        var advertisementItemCard: LinearLayout? = view.findViewById(R.id.advertisement_item_card)
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}