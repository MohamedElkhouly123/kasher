package com.example.ecommerceapp.adapter

import android.animation.LayoutTransition
import android.content.ContentValues
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
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
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

class BinAdapter(
    var binList: MutableList<AllProductForRom>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<BinAdapter.BinViewHolder>() {
    private var generalBundelDataToSend: GeneralBundelDataToSend?=null
    private var bundle: Bundle?=null
    private val activity: BaseActivity?
    private val context: Context
    private var dataBase: DataBaseKotlin? = null
    private lateinit var allProductForRom: AllProductForRom
    private var itemId: Int = 0
    private var items: List<AllProductForRom?>?= ArrayList<AllProductForRom>()
    var navController: NavController
    var open = false
    var totalProductsPrice: Int=0
    var totalShippingPrice: Int=0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BinViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_bin_store, null, false)
        return BinViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BinViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: BinViewHolder, position: Int) {
        var binItem = binList.get(position)

        holder.cardBinStoreIncreaseNumberProduct?.setOnClickListener {
            holder.productItemCount++
            binList.get(position).quantity=holder.productItemCount.toString()
            GlobalScope.launch { Dispatchers.IO
                Dispatchers.IO  // equal newSingleThreadExecutor
                dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(), binItem.itemId)
                try {
                    Log.i(
                        ContentValues.TAG + "" + items?.size,
                        dataBase!!.addNewOrderItemDao()!!.loadSingle(binItem.itemId).quantity
                    )
                    Log.i(ContentValues.TAG + "" + items?.size, binItem.itemId.toString())
                }catch (e:Exception){}
            }
            holder.cardBinStoreNumberOfProduct?.text=""+holder.productItemCount
            binItem.quantity=holder.productItemCount.toString()
            holder.cardBinStorePriceOfTotalProducts?.text= (binItem.productPrice.toInt()*binItem.quantity.toInt()).toString()+"  "+activity!!.getString(R.string.Reial)
            totalProductsPrice+=binItem.productPrice.toInt()
            generalDataSendedModel.makeChangeInFragment!!.doChanges(generalDataSendedModel)
            checkIcon(position,holder)
        }
        holder.cardBinStoreDecreaseNumberProduct?.setOnClickListener {
            if(holder.productItemCount>0) {
                if(holder.productItemCount==1) {
                    deleteBinItem(position,holder)
                }else{
                    holder.productItemCount--
                    Executors.newSingleThreadExecutor()
                        .execute {
                            dataBase!!.addNewOrderItemDao()!!.update(holder.productItemCount.toString(),binItem.itemId)
                            dataBase!!.addNewOrderItemDao()!!.loadSingle(binItem.itemId)
                        }
                }
                binList.get(position).quantity=holder.productItemCount.toString()
                holder.cardBinStoreNumberOfProduct?.text=""+holder.productItemCount
                binItem.quantity=holder.productItemCount.toString()
                holder.cardBinStorePriceOfTotalProducts?.text= (binItem.productPrice.toInt()*binItem.quantity.toInt()).toString()+"  "+activity!!.getString(R.string.Reial)
                totalProductsPrice-=binItem.productPrice.toInt()
                generalDataSendedModel.makeChangeInFragment!!.doChanges(generalDataSendedModel)

            }
            checkIcon(position,holder)
        }
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        bundle?.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        holder.cardParent!!.setOnClickListener {
            try {
                navController.navigate(R.id.productDetailsFragment,bundle)
            } catch (e: Exception) { }
        }
        holder.card_bin_store_bottom_cart_delete_btn!!.setOnClickListener {
                deleteBinItem(position,holder)
        }

        holder.cart_bin_decrease_number_delete_img!!.setOnClickListener {
            deleteBinItem(position,holder)
        }
        holder.cardParent!!.setOnClickListener {
//            val v = if (holder.textDtails!!.visibility == View.GONE) View.VISIBLE else View.GONE
//            TransitionManager.beginDelayedTransition(
//                holder.linearParent,
//                AutoTransition()
//            )
//            holder.textDtails!!.visibility = v


            //                if(!open){
            //
            //
            //
            //                Toast.makeText(context, "" + MATCH_PARENT, Toast.LENGTH_SHORT).show();
            //                ValueAnimator anim = ValueAnimator.ofInt(holder.cardParent.getMeasuredHeight(), 1500);
            //                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //                    @Override
            //                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
            //                        int val = (Integer) valueAnimator.getAnimatedValue();
            //                        ViewGroup.LayoutParams layoutParams = holder.cardParent.getLayoutParams();
            //                        layoutParams.height = val;
            //                        holder.cardParent.setLayoutParams(layoutParams);
            //                        open=true;
            //                    }
            //                });
            //                anim.setDuration(3000);
            //                anim.start();
            //                }else {
            //                    ValueAnimator anim2 = ValueAnimator.ofInt(holder.cardParent.getMeasuredHeight(), WRAP_CONTENT);
            //                    anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //                        @Override
            //                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            //                            int val = (Integer) valueAnimator.getAnimatedValue();
            //                            ViewGroup.LayoutParams layoutParams = holder.cardParent.getLayoutParams();
            //                            layoutParams.height = val;
            //                            holder.cardParent.setLayoutParams(layoutParams);
            //                        }
            //                    });
            //                    anim2.setDuration(3000);
            //                    anim2.start();
            //                    open=false;
            //                }
        }
    }

    private fun deleteBinItem(position: Int, holder: BinViewHolder) {
        var binItem = binList.get(position)
        Executors.newSingleThreadExecutor()
            .execute {
                dataBase!!.addNewOrderItemDao()!!.deleteById(binItem.itemId)
                runBlocking (Dispatchers.Main) {
                    binList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(
                        0,
                        getItemCount()
                    )
                    if(binList.size==0){
                        generalDataSendedModel!!.no_products?.visibility=View.VISIBLE
                    }
                    totalShippingPrice-=binItem.productPrice.toInt()
                }
            }
        holder.productItemCount--
    }

    private fun setData(holder: BinViewHolder, position: Int) {
        holder.linearParent!!.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        dataBase = DataBaseKotlin.getInstance(activity!!)
        var binItem = binList.get(position)
        if (!"info".equals(generalDataSendedModel.type)){
            holder.cardBinStoreIncreaseNumberProduct?.visibility=View.VISIBLE
            holder.cardBinStoreDecreaseNumberProduct?.visibility=View.VISIBLE
        }else{
            holder.cardBinStoreIncreaseNumberProduct?.visibility=View.GONE
            holder.cardBinStoreDecreaseNumberProduct?.visibility=View.GONE
            holder.cardBinStoreNumberOfProduct?.text= activity.getString(R.string.number)+"  "+binItem.quantity
        }
        if (binItem.quantity.equals("0")){

        }else{
            holder.productItemCount=binItem.quantity.toInt()
            holder.cardBinStoreNumberOfProduct?.text=""+holder.productItemCount
        }
        holder.binCardNameProductTv?.text=binItem.productName
        holder.cardBinStorePriceOfOneProduct?.text=binItem.productPrice
        var productTotalPrice = binItem.productPrice.toInt() * binItem.quantity.toInt()
        holder.cardBinStorePriceOfTotalProducts?.text= (productTotalPrice).toString()+"  "+activity!!.getString(R.string.Reial)
        totalProductsPrice+=productTotalPrice
        totalShippingPrice+=binItem.productPrice.toInt()

        Executors.newSingleThreadExecutor()
            .execute {
                try {
                    var name = dataBase!!.addNewOrderItemDao()!!.loadSingleByName("ali").productName
                    Log.i(ContentValues.TAG + "" + items?.size, name)
                }catch (e:Exception){}
            }
        checkIcon(position,holder)
    }

    private fun checkIcon(position: Int, holder: BinViewHolder) {
        if (!"info".equals(generalDataSendedModel.type)) {
            holder!!.card_bin_store_bottom_cart_delete_btn.visibility = View.VISIBLE
            if(holder.productItemCount==1) {
            holder.cardBinStoreDecreaseNumberProduct?.visibility=View.GONE
                holder!!.cart_bin_decrease_number_delete_img.visibility = View.VISIBLE
            }else{
                holder.cart_bin_decrease_number_delete_img.visibility=View.GONE
                holder.cardBinStoreDecreaseNumberProduct?.visibility = View.VISIBLE
            }
        }else{
//            root!!.fragment_product_details_decrease_number_img.setImageResource(R.drawable.ic_mixnus_icon)
        }
    }

    override fun getItemCount(): Int {
        return binList.size
    }

    inner class BinViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {


        val cart_bin_decrease_number_img: ImageView=view.findViewById(R.id.cart_bin_decrease_number_img)
        val cart_bin_decrease_number_delete_img: ImageView=view.findViewById(R.id.cart_bin_decrease_number_delete_img)
        val card_bin_store_bottom_cart_delete_btn: ImageView=view.findViewById(R.id.card_bin_store_bottom_cart_delete_btn)

        var productItemCount=0
        var cardBinStoreImageProduct: ImageView? = view.findViewById(R.id.card_bin_store_image_product)
        var binCardNameProductTv: TextView? = view.findViewById(R.id.bin_card_name_product_tv)
        var cardBinStorePriceOfOneProduct: TextView? = view.findViewById(R.id.card_bin_store_price_of_one_product)
        var binCardOnePriceLayout: LinearLayout? = view.findViewById(R.id.bin_card_one_price_layout)
        var cardBinStorePriceOfTotalProducts: TextView? = view.findViewById(R.id.card_bin_store_price_of_total_products)
        var favouriteCardPriceLayout: LinearLayout? = view.findViewById(R.id.favourite_card_price_layout)
        var favouriteCardNoPriceTv: TextView? = view.findViewById(R.id.favourite_card_no_price_tv)
        var cardBinStoreIncreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_bin_store_increase_number_product)
        var cardBinStoreNumberOfProduct: TextView? = view.findViewById(R.id.card_bin_store_number_of_product)
        var cardBinStoreDecreaseNumberProduct: FrameLayout? = view.findViewById(R.id.card_bin_store_decrease_number_product)
        var cardParent: CardView? = view.findViewById(R.id.card_parent)
        var linearParent: FrameLayout? = view.findViewById(R.id.linear_parent)
        var textDtails: TextView? = view.findViewById(R.id.text_dtails)



//        fun bindata(text: SingleText){
//            title.text = text.title
//            desc.text = text.desc
//        }
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}