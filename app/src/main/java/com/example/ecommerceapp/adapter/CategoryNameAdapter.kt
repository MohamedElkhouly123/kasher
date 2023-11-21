package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.CategoryNameAdapter.CategoryNameViewHolder
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.utils.Post

class CategoryNameAdapter(var categoryNmaeList: List<Post>, var generalDataSendedModel: GeneralDataSendedModel) :
    RecyclerView.Adapter<CategoryNameViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController? = null
    private val payload: String? = null
    private val first = true
    var selectedItem = -1
    var vindorId = -1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryNameViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_category_name, null, false)
        return CategoryNameViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CategoryNameViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: CategoryNameViewHolder, position: Int) {}
    private fun setData(holder: CategoryNameViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class CategoryNameViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {

    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
    }
}