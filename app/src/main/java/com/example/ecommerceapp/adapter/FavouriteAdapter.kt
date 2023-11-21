package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.FavouriteAdapter.FavouriteViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import com.example.ecommerceapp.view.main.data.models.AllProductForRom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class FavouriteAdapter(
    var favouriteList: MutableList<AllProductForRom>,var generalDataSendedModel: GeneralDataSendedModel

) :
    RecyclerView.Adapter<FavouriteViewHolder>() {
    private var bundle: Bundle?=null
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private val activity: BaseActivity?
    private val context: Context
    private var dataBase: DataBaseKotlin? = null
    private lateinit var allProductForRom: AllProductForRom
    private var itemId: Int = 0
    private var items: List<AllProductForRom?>?= ArrayList<AllProductForRom>()
    var navController: NavController
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_favourite, null, false)
        return FavouriteViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: FavouriteViewHolder, position: Int) {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend?.from="favourite"
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        holder.favouriteCardAddToBinBtu!!.setOnClickListener {
            holder.favouriteCardAddToBinBtu!!.visibility = View.GONE
            holder.cardFavouriteItemAddToBinLayout!!.visibility = View.VISIBLE
        }

        holder.cardParent!!.setOnClickListener {
            try {
                navController.navigate(R.id.productDetailsFragment,bundle)
            } catch (e: Exception) {
            }
        }
        holder.favouriteCardRemoveFavouriteBtu!!.setOnClickListener {
            Executors.newSingleThreadExecutor()
                .execute {
                    //            showToast(getActivity(), notificationPosition + "");
                    runBlocking (Dispatchers.Main) {
                        holder.view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rv_animation_left_to_right2));
                        Handler().postDelayed({
                            if (favouriteList.size>0&&favouriteList.size>position) {
                                favouriteList?.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(
                                    0,
                                    getItemCount()
                                )
                            }
//                            if (favouriteList.size == 0) {
//                                makeChangeInFragment.doChanges()
//                            }
                        }, 600)
                    }
                }
        }

        holder.favouriteCardAddToBinBtu!!.setOnClickListener {
            holder.favouriteCardAddToBinBtu!!.visibility = View.GONE
            holder.cardFavouriteItemAddToBinLayout!!.visibility = View.VISIBLE
            holder.productItemCount=1
            favouriteList.get(position).quantity=holder.productItemCount.toString()
            holder.cardAllMoreSellerItemNumberOfProduct?.text=""+holder.productItemCount
            Executors.newSingleThreadExecutor()
                .execute {
                    dataBase!!.addNewOrderItemDao()!!.insert(favouriteList.get(position))
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
            favouriteList.get(position).quantity=holder.productItemCount.toString()
            GlobalScope.launch { Dispatchers.IO
                Dispatchers.IO  // equal newSingleThreadExecutor
                dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(), itemId)
//                Log.i(ContentValues.TAG +""+items?.size, dataBase!!.addNewOrderItemDao()!!.loadSingle(itemId).quantity)
//                Log.i(ContentValues.TAG+""+items?.size, itemId.toString())
            }
            holder.cardAllMoreSellerItemNumberOfProduct?.text=""+holder.productItemCount

        }
        holder.cardAllMoreSellerItemDecreaseNumberProduct?.setOnClickListener {
            if(holder.productItemCount>0) {
                if(holder.productItemCount==1) {
                    holder.cardFavouriteItemAddToBinLayout!!.visibility = View.GONE
                    holder.favouriteCardAddToBinBtu!!.visibility = View.VISIBLE
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
                favouriteList.get(position).quantity=holder.productItemCount.toString()
                holder.cardAllMoreSellerItemNumberOfProduct?.text=""+holder.productItemCount

            }
        }
    }

    private fun setData(holder: FavouriteViewHolder, position: Int) {
        dataBase = DataBaseKotlin.getInstance(activity!!)
        var favouriteItem = favouriteList.get(position)
        if (favouriteItem.quantity.equals("0")){
//            holder.favouriteCardAddToBinBtu!!.visibility = View.VISIBLE
            holder.cardFavouriteItemAddToBinLayout!!.visibility = View.GONE
        }else{
            holder.favouriteCardAddToBinBtu!!.visibility = View.GONE
            holder.cardFavouriteItemAddToBinLayout!!.visibility = View.VISIBLE
            holder.productItemCount=favouriteItem.quantity.toInt()
            holder.cardAllMoreSellerItemNumberOfProduct?.text=""+holder.productItemCount
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
        return favouriteList.size
    }

    inner class FavouriteViewHolder(val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var productItemCount=0

        var categoryNameCardImage: ImageView? = view.findViewById(R.id.category_name_card_image)
        var cardMoreSellerFillHeart: ImageView? = view.findViewById(R.id.card_more_seller_fill_heart)
        var favouriteCardRemoveFavouriteBtu: FrameLayout? = view.findViewById(R.id.favourite_card_remove_favourite_btu)
        var favouriteCardNameProductTv: TextView? = view.findViewById(R.id.favourite_card_name_product_tv)
        var favouriteCardPriceProductTv: TextView? = view.findViewById(R.id.favourite_card_price_product_tv)
        var favouriteCardPriceLayout: LinearLayout? = view.findViewById(R.id.favourite_card_price_layout)
        var favouriteCardNoPriceTv: TextView? = view.findViewById(R.id.favourite_card_no_price_tv)
        var cardAllMoreSellerItemIncreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_all_more_seller_item_increase_number_product)
        var cardAllMoreSellerItemNumberOfProduct: TextView? = view.findViewById(R.id.card_all_more_seller_item_number_of_product)
        var cardAllMoreSellerItemDecreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_all_more_seller_item_decrease_number_product)
        var cardFavouriteItemAddToBinLayout: LinearLayout? = view.findViewById(R.id.card_favourite_item_add_to_bin_layout)
        var favouriteCardAddToBinBtu: TextView? = view.findViewById(R.id.favourite_card_add_to_bin_btu)
        var favouriteCardParent: LinearLayout? = view.findViewById(R.id.favourite_card_parent)
        var cardParent: CardView? = view.findViewById(R.id.card_parent)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}