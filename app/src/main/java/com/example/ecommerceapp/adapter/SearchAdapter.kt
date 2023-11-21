package com.example.ecommerceapp.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.SearchAdapter.SearchViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.data.model.getGeneralResponse.SearchesListForRoom
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.main.data.local.DataBaseKotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

class SearchAdapter(
    var searchesListForRoom: MutableList<SearchesListForRoom>,
    var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<SearchViewHolder>() {
    private var dataBase: DataBaseKotlin? = null
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_search_product, null, false)
        return SearchViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: SearchViewHolder, position: Int) {
        var searchItem = searchesListForRoom[position]

        holder.cardSearchDeleteIconImageBtn?.setOnClickListener {
            if (holder.cardSearchDeleteIconImage?.visibility==View.VISIBLE){
            Executors.newSingleThreadExecutor()
                .execute {
                    dataBase!!.addNewOrderItemDao()!!.deleteSearchesById(searchItem.itemId)
                    //            showToast(getActivity(), notificationPosition + "");
                    runBlocking(Dispatchers.Main) {
                        holder.view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rv_animation_left_to_right2));
                        Handler().postDelayed({
                            if (searchesListForRoom.size>0&&searchesListForRoom.size>position) {
                                searchesListForRoom?.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(
                                    0,
                                    getItemCount()
                                )
                            }
                        }, 600)

                    }
                }
            }
        }
        holder.view.setOnClickListener {
            generalDataSendedModel?.productAdaptersActions?.makeActionSearch(generalDataSendedModel)
        }



    }
    private fun setData(holder: SearchViewHolder, position: Int) {
        dataBase = DataBaseKotlin.getInstance(activity!!)
        var searchItem = searchesListForRoom[position]
        holder.cardSearchProductNameTv?.text=searchItem.searchTextToSave
        if("server".equals(generalDataSendedModel?.generalBundelDataToSend?.adapterType)) {
            holder.cardSearchArrowIconImage?.visibility = View.VISIBLE
            holder.cardSearchDeleteIconImage?.visibility = View.GONE
        }else{
            holder.cardSearchArrowIconImage?.visibility = View.GONE
            holder.cardSearchDeleteIconImage?.visibility = View.VISIBLE
        }
        if (getItemViewType(position) == 0) {
            holder.topImage!!.visibility = View.GONE
        } else {
            holder.topImage!!.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return searchesListForRoom.size
    }

    inner class SearchViewHolder(val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var topImage: ImageView? = view.findViewById(R.id.top_image)
        var cardSearchProductNameTv: TextView? = view.findViewById(R.id.card_search_product_name_tv)
        var cardSearchDeleteIconImageBtn: LinearLayout? = view.findViewById(R.id.card_search_delete_icon_image_btn)
        var cardSearchDeleteIconImage: ImageView? = view.findViewById(R.id.card_search_delete_icon_image)
        var cardSearchArrowIconImage: ImageView? = view.findViewById(R.id.card_search_arrow_icon_image)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}