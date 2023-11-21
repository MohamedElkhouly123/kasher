package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.CategoryAdapter.CategoryViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralBundelDataToSend
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post


class CategoryAdapter(
    var categoryList: List<Post>,
   var generalDataSendedModel: GeneralDataSendedModel
) :
    RecyclerView.Adapter<CategoryViewHolder>() {
    private lateinit var generalBundelDataToSend: GeneralBundelDataToSend
    private lateinit var bundle: Bundle
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    var right = 1
    var left = 2
    var v: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        v = if (viewType == right) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_category_right, null, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_category_left, null, false)
        }
        return CategoryViewHolder(v!!)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            right
        } else {
            left
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: CategoryViewHolder, position: Int) {
        bundle = Bundle()
        generalBundelDataToSend = GeneralBundelDataToSend()
        generalBundelDataToSend.from="cat"
        bundle.putSerializable("generalBundelDataToSend",generalBundelDataToSend)
        holder.cardParent!!.setOnClickListener { navController.navigate(R.id.subCategoryFragment,bundle) }
    }

    private fun setData(holder: CategoryViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 10
    }


    open inner class CategoryViewHolder(  //        @Nullable
        //        @BindView(R.id.card_category_right_image)
        //        ImageView cardCategoryRightImage;
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        //        @Nullable
        //        @BindView(R.id.card_category_right_name_tv)
        //        TextView cardCategoryRightNameTv;
        //        @Nullable
        //        @BindView(R.id.card_category_right_details_tv)
        //        TextView cardCategoryRightDetailsTv;
        var cardParent: CardView? =view.findViewById(R.id.card_parent)

    }

    internal inner class CategoryViewHolderLeft(private val view: View) : CategoryViewHolder(view) {
        var cardParent2: CardView? = view.findViewById(R.id.card_parent)

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}