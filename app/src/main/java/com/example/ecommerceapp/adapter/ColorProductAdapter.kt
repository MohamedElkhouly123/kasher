package com.example.ecommerceapp.adapter

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

import com.example.ecommerceapp.R
import com.example.ecommerceapp.adapter.ColorProductAdapter.ColorProductViewHolder
import com.example.ecommerceapp.data.model.Color
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.BaseActivity
import de.hdodenhof.circleimageview.CircleImageView

class ColorProductAdapter(
    var colorProductList: List<Color>,
    var generalDataSendedModel: GeneralDataSendedModel
) :


    RecyclerView.Adapter<ColorProductViewHolder>() {
    private val activity: BaseActivity?
    private val context: Context
    var navController: NavController
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorProductViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.card_color_mark, null, false)
        return ColorProductViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ColorProductViewHolder, position: Int) {
        setData(holder, position)
        setAction(holder, position)
    }

    private fun setAction(holder: ColorProductViewHolder, position: Int) {
        holder.colorCard!!.setOnClickListener {
            holder.colorCardOutLineBorder!!.visibility = View.VISIBLE
        }
    }

    private fun setData(holder: ColorProductViewHolder, position: Int) {
        holder.colorCardFillCircle!!.setImageDrawable(ColorDrawable(colorProductList[position].color))
    }

    override fun getItemCount(): Int {
        return colorProductList.size
    }

    inner class ColorProductViewHolder(private val view: View) : RecyclerView.ViewHolder(
        view
    ) {
        var colorCardOutLineBorder: ImageView? = view.findViewById(R.id.color_card_out_line_border)
        var colorCardFillCircle: CircleImageView? = view.findViewById(R.id.color_card_fill_circle)
        var colorCard: FrameLayout? = view.findViewById(R.id.color_card)
    }

    init {
        this.activity = generalDataSendedModel.activity as BaseActivity?
        this.context = generalDataSendedModel?.context!!
        this.navController = generalDataSendedModel?.navController!!
    }
}